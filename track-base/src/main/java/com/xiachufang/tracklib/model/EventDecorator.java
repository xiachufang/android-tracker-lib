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

        if (TrackManager.getSendControler()!=null&&TrackManager.getSendControler().shouldSend()){
            //符合发送条件
            TrackPushService.getInstance().excutePushEvent();
        }
    }

}
