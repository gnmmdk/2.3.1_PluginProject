package com.kangjj.plugin.stander;

import android.app.Activity;
import android.os.Bundle;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.3.1_PluginProject
 * @Package: com.kangjj.plugin.stander
 * @CreateDate: 2019/11/12 16:05
 */
public interface ActivityInterface {
    /**
     * 把宿主（app）的环境给插件
     * @param appActivity
     */
    void insertAppContext(Activity appActivity);

    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onDestroy();
}
