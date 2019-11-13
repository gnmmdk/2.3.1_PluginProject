package com.kangjj.plugin.packages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kangjj.plugin.stander.ReceiverInterface;

public class PluginReceiver extends BroadcastReceiver implements ReceiverInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            if(intent.getAction()== PluginActivity.ACTION){
                Toast.makeText(context, "插件接收到广播啦", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
