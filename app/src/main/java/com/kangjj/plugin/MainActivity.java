package com.kangjj.plugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 加载插件
    public void loadPlugin(View view) {
        boolean ret = PluginManager.getInstatnce(this).loadPlugin();
        Toast.makeText(this, ret?"插件加载成功":"插件加载失败", Toast.LENGTH_SHORT).show();
    }

    // 启动插件里面的Activity
    public void startPluginActivity(View view) {
        File file = new File("/sdcard/kangjj.plugin"/*Environment.getExternalStorageDirectory() + File.separator + "kangjj.plugin"*/);
        if (!file.exists()) {
            return;
        }
        String path = file.getAbsolutePath();

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        ActivityInfo activityInfo = packageInfo.activities[0];

        //占位
        Intent intent = new Intent( this,ProxyActivity.class);
        intent.putExtra("className",activityInfo.name);
        startActivity(intent);
    }

    public void loadStaticReceiver(View view) {
        PluginManager.getInstatnce(this).parserApkAction();
    }

    public void sendStaticReceiver(View view) {
        Intent intent = new Intent();
//        intent.setAction("plugin.static_receiver");
        intent.setAction("plugin.static_receiver2");
        sendBroadcast(intent);
    }
}
