package com.kangjj.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.kangjj.plugin.stander.ActivityInterface;

import java.lang.reflect.Constructor;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.3.1_PluginProject
 * @Package: com.kangjj.plugin.packages
 * @CreateDate: 2019/11/12 17:21
 */
public class ProxyActivity extends Activity {

    @Override
    public Resources getResources(){
        return PluginManager.getInstatnce(this).getResources();
    }

    @Override
    public ClassLoader getClassLoader(){
        return PluginManager.getInstatnce(this).getClassLoader();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 真正的加载 插件里面的 Activity
        String className = getIntent().getStringExtra("className");
        try {
            Class mPluginActivityClass = getClassLoader().loadClass(className);
            Constructor constructor = mPluginActivityClass.getConstructor(new Class[]{});
            Object mPluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface)mPluginActivity;
            // 注入
            activityInterface.insertAppContext((this));
            Bundle bundle = new Bundle();
            bundle.putString("appName","我是宿主传递过来的信息");

            // 执行插件里面的onCreate方法
            activityInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent proxyIntent = new Intent(this,ProxyActivity.class);
        proxyIntent.putExtra("className",className);
        //要给TestActivity进栈
        super.startActivity(proxyIntent);
    }
}
