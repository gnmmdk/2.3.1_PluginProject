package com.kangjj.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.kangjj.plugin.stander.ServiceInterface;

public class ProxyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("className");
        try {
            Class clazz = PluginManager.getInstatnce(this).getClassLoader().loadClass(className);
            Object mService = clazz.newInstance();
            ServiceInterface serviceInterface = (ServiceInterface) mService;
            serviceInterface.insertService(this);
            serviceInterface.onStartCommand(intent,flags,startId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
