package com.kangjj.plugin.packages;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.kangjj.plugin.stander.ServiceInterface;

public class BaseService extends Service implements ServiceInterface {
    private Service appService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void insertService(Service appService) {
        this.appService = appService;
    }

    @Override
    public void onCreate() {
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
    }
}
