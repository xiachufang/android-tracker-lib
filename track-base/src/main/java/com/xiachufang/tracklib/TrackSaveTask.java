package com.xiachufang.tracklib;

import android.text.TextUtils;

import com.xiachufang.tracklib.db.TrackDBManager;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.util.GlobalParams;


/**
 * creator huangyong
 * createTime 2018/11/14 上午11:06
 * path com.xiachufang.tracklib.controler
 * description: 存储的任务
 */
public class TrackSaveTask implements Runnable {



    private String trackUrl;

    public TrackSaveTask(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    @Override
    public void run() {

        if (!TrackManager.hasInit) {
            return;
        }

        if (!GlobalParams.SWITCH_ON) {
            return;
        }

        try {

            if (TextUtils.isEmpty(trackUrl)) {
                return;
            }
            //存储数据
            TrackData trackData = EventDecorator.generateEventParams(trackUrl);

            //插入数据到本地数据库
            TrackDBManager.addEventData(trackData);

            //内存计数递增
            EventDecorator.pushEventByNum();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        return "XCFEventTask{" +
                '}';
    }
}
