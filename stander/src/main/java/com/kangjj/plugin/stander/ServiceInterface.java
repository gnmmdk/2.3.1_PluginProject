package com.kangjj.plugin.stander;

import android.app.Service;
import android.content.Intent;

public interface ServiceInterface {
    /**
     * 把宿主(app)的环境  给  插件
     * @param appService
     */
    void insertService(Service appService);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();
}
