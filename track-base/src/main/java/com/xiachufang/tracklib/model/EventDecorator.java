package com.xiachufang.tracklib.model;

import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.services.TrackPushService;
import com.xiachufang.tracklib.util.GlobalParams;
import com.xiachufang.tracklib.util.Logs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * creator huangyong
 * createTime 2018/11/14 下午12:15
 * path com.xiachufang.tracklib.util
 * description:
 */
public class EventDecorator {

    /**
     * 日志时间 时间戳
     *
     * @return
     */
    public static synchronized long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static synchronized void addEventNum() {
        TrackManager.getSendControler().inCrease();
    }

    /**
     * 目前是逐条操作，所以执行减1
     */
    public static synchronized void decreaseNum() {
        TrackManager.getSendControler().deCrease();
    }

    public static synchronized void clearNum() {
        TrackManager.getSendControler().reset();
    }

    /**
     * 将传入参数打包封装成需要传送的格式
     *
     * @param trackUrl
     * @return
     */
    public static TrackData generateEventParams(String trackUrl) {
        TrackData data = new TrackData();
        long timestamp = System.currentTimeMillis();
        data.setTrackData(trackUrl);
        data.setTimeStamp(timestamp);
        return data;
    }


    /**
     * 当操作数大于100条，则执行上传
     */
    public static void pushEventByNum() {
        TrackManager.getSendControler().inCrease();
        Logs.d("event-->满足连续操作大于100,开始上传");
        if (TrackManager.getSendControler()!=null&&TrackManager.getSendControler().shouldSend()){
            //符合发送条件
            TrackPushService.getInstance().excutePushEvent();
        }
    }

}
