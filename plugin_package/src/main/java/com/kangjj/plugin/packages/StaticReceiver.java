package com.kangjj.plugin.packages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("plugin.static_receiver".equals(intent.getAction())){
            Toast.makeText(context, "我是静态注册的广播1，我收到广播了", Toast.LENGTH_SHORT).show();
        }else if("plugin.static_receiver2".equals(intent.getAction())){
            Toast.makeText(context, "我是静态注册的广播2，我收到广播了", Toast.LENGTH_SHORT).show();
        }
    }
}
