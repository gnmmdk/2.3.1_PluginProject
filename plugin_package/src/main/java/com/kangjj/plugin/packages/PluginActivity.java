package com.kangjj.plugin.packages;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PluginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        TextView view = (TextView) findViewById(R.id.tv_content);
        view.setText(R.string.app_plugin);

        findViewById(R.id.bt_start_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里的TestActivity不需要在AndroidManifest注册
                startActivity(new Intent(appActivity,TestActivity.class));
            }
        });
    }
}
