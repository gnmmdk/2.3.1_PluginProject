package com.kangjj.plugin.packages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kangjj.plugin.stander.ActivityInterface;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.3.1_PluginProject
 * @Package: com.kangjj.plugin.packages
 * @CreateDate: 2019/11/12 16:09
 */
public class BaseActivity extends Activity implements ActivityInterface {
    protected Activity appActivity;

    @Override
    public void insertAppContext(Activity appActivity) {
        this.appActivity = appActivity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle saveInstanceState) {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    public void setContentView(int layoutId){
        appActivity.setContentView(layoutId);
    }

    public View findViewById(int viewId){
        return appActivity.findViewById(viewId);
    }

    public void startActivity(Intent intent){
        Intent intentNew = new Intent();
        intentNew.putExtra("className",intent.getComponent().getClassName());
        appActivity.startActivity(intentNew);
    }
}
