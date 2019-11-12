package com.kangjj.plugin.packages;


import android.os.Bundle;
import android.widget.TextView;

public class PluginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        TextView view = (TextView) findViewById(R.id.tv_content);
        view.setText(R.string.app_plugin);
    }
}
