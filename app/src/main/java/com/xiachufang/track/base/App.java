package com.xiachufang.track.base;

import android.app.Application;

import com.xiachufang.tracklib.TrackManager;

/**
 * creator huangyong
 * createTime 2018/12/4 下午3:57
 * path com.xiachufang.track.base
 * description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化sdk
        TrackManager.Config mConfig = new TrackManager.Config(this)
                .setDebug(true)
                .setApplication(this)
                .setHeadConfig(new HeaderConfig())
                .setPushLimitMinutes(1)
                .setPushLimitNum(100)
                .init();
        TrackManager.setConfig(mConfig);
    }
}
