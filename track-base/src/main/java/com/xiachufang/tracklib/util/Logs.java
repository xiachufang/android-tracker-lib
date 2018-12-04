package com.xiachufang.tracklib.util;

import android.content.Intent;

import com.xiachufang.tracklib.TrackManager;


/**
 * creator huangyong
 * createTime 2018/11/14 下午3:55
 * path com.xiachufang.tracklib.util
 * description:
 */
public class Logs {


    public static void d(String p){
        if (GlobalParams.DEVELOP_MODE){
            android.util.Log.d(GlobalParams.TAG,p);
            Intent intent = new Intent();
            intent.setAction(GlobalParams.ACTION_LOG);
            TrackManager.getContext().sendBroadcast(intent);
        }
    }
}
