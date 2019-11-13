package com.kangjj.plugin.packages;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class PluginActivity extends BaseActivity {

    public static final String ACTION = "com.netease.plugin_package.ACTION";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        findViewById(R.id.bt_start_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里的TestActivity不需要在AndroidManifest注册
                startActivity(new Intent(appActivity,TestActivity.class));
            }
        });

        findViewById(R.id.bt_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(appActivity, PluginService.class));
            }
        });

        findViewById(R.id.bt_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION);
                registerReceiver(new PluginReceiver(),intentFilter);
            }
        });

        findViewById(R.id.bt_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });
    }
}
