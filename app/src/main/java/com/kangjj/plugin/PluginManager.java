package com.kangjj.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.3.1_PluginProject
 * @Package: com.kangjj.plugin
 * @CreateDate: 2019/11/12 16:22
 */
public class PluginManager {
    private static final String TAG = PluginManager.class.getSimpleName();

    private static PluginManager pluginManager;
    private final Context context;
    private DexClassLoader dexClassLoader;
    private Resources resources;

    public static PluginManager getInstatnce(Context context) {
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                if (pluginManager == null) {
                    pluginManager = new PluginManager(context);
                }
            }
        }
        return pluginManager;
    }

    private PluginManager(Context context) {
        this.context = context;
    }

    public boolean loadPlugin() {
        try {
            File file = new File("/sdcard/kangjj.plugin"/*Environment.getExternalStorageDirectory() + File.separator + "kangjj.plugin"*/);
            if (!file.exists()) {
                return false;
            }
            String pluginPath = file.getAbsolutePath();
            /**
             * 加载插件里面的class
             */
            //dexClassLoader 需要一个缓存目录 /data/data/包名/pluginDir
            File fileDir = context.getDir("pluginDir", Context.MODE_PRIVATE);

            // Activity class
            dexClassLoader = new DexClassLoader(
                    pluginPath, fileDir.getAbsolutePath(),
                    null, context.getClassLoader());

            /**
             * 加载插件里面的资源 （layout、resource)
             */

            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);
            Resources r = context.getResources();//宿主的资源配置信息

            //特殊的Resources，加载插件里面地资源Resource
            resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());// 参数2 3  资源配置信息
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ClassLoader getClassLoader(){
        return dexClassLoader;
    }

    public Resources getResources(){
        return resources;
    }

    /**
     *    反射5.0 系统源码，来解析apk文件里面的所有信息 9.0的源码也可以用 不过手机是8.0的系统 应该没有影响。
     */
    public void parserApkAction(){
        try {
            File file = new File("/sdcard/kangjj.plugin"/*Environment.getExternalStorageDirectory() + File.separator + "kangjj.plugin"*/);
            if (!file.exists()) {
                Log.d(TAG, "插件包 不存在...");
                return ;
            }
            //实例化PackageParser对象
            Class mPackageParserClass = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser= mPackageParserClass.newInstance();
            // 1 执行此方法  public Package parsePackage(File packageFile, int flags)  就是为了，拿到Package
            Method mParsePackageMethod = mPackageParserClass.getMethod("parsePackage",File.class,int.class);
            Object mPackage = mParsePackageMethod.invoke(mPackageParser,file, PackageManager.GET_ACTIVITIES);
            // 继续分析Package
            // 得到receivers    public final ArrayList<Activity> receivers = new ArrayList<Activity>(0);
            Field  mReceiversField=mPackage.getClass().getDeclaredField("receivers");   //getField 获取一个类的 ==public成员变量，包括基类== 。 getDeclaredField 获取一个类的 ==所有成员变量，不包括基类== 。
            //receivers 的本质是 ArrayList<Activity> 这里的Activity是PackageParser里面的内部类
            Object receivers = mReceiversField.get(mPackage);
            ArrayList arrayList = (ArrayList) receivers;
            // 此Activity 不是组件的Activity，是PackageParser里面的内部类
            for (Object mActivity : arrayList) { // mActivity --> <receiver android:name=".StaticReceiver">
                // 获取 <intent-filter>    intents== 很多 Intent-Filter
                // 通过反射拿到intents
                Class mComponentClass= Class.forName("android.content.pm.PackageParser$Component");
                Field intentsField=mComponentClass.getDeclaredField("intents");  // public final ArrayList<II> intents;
                ArrayList<IntentFilter> intents = (ArrayList) intentsField.get(mActivity);  // intents 所属的对象是谁 ?   ->mActivity
                //至此拿到了<intent-filter>，还需要拿到 android:name=".StaticReceiver"
                // activityInfo.name; == android:name=".StaticReceiver"
                // 分析源码 如何 拿到 ActivityInfo
                /*Field mActivityInfoField = mActivity.getClass().getDeclaredField("info");   //这种方式也可以拿到
                ActivityInfo mActivityInfo= (ActivityInfo) mActivityInfoField.get(mActivity); */

                Class UserHandleClass = Class.forName("android.os.UserHandle");
                Method getCallingUserIdMethod = UserHandleClass.getMethod("getCallingUserId");
                int userId = (int) getCallingUserIdMethod.invoke(null);

                Class mPackageUserStateClass = Class.forName("android.content.pm.PackageUserState");
                // public static final ActivityInfo generateActivityInfo(Activity a, int flags, PackageUserState state, int userId)
                Method generateActivityInfoMethod = mPackageParserClass.getMethod("generateActivityInfo",mActivity.getClass(),int.class, mPackageUserStateClass,int.class);
//                generateActivityInfoMethod.setAccessible(true);//这里可以去掉 因为该方法是public
                ActivityInfo mActivityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null,mActivity,0,mPackageUserStateClass.newInstance(),userId);
                String receiverClassName = mActivityInfo.name;//com.kangjj.plugin.packages.StaticReceiver

                Class mStaticReceiverClass = getClassLoader().loadClass(receiverClassName);
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) mStaticReceiverClass.newInstance();
                for (IntentFilter intentFilter : intents) {
                    //注册广播
                    context.registerReceiver(broadcastReceiver,intentFilter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
