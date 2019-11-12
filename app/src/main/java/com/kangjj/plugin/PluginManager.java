package com.kangjj.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

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
}
