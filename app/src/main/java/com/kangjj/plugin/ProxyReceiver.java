package com.kangjj.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kangjj.plugin.stander.ReceiverInterface;

public class ProxyReceiver extends BroadcastReceiver {
    private String className;
    public ProxyReceiver(String className) {
        this.className = className;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Class clazz = PluginManager.getInstatnce(context).getClassLoader().loadClass(className);
            Object pluginReceiver = clazz.newInstance();
            ReceiverInterface receiverInterface= (ReceiverInterface) pluginReceiver;
            receiverInterface.onReceive(context,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
