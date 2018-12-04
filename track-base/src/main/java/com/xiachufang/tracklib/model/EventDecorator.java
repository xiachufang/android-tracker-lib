package com.xiachufang.tracklib.model;

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

    //当满足连续操作大于100条,就进行上传服务
    private static final AtomicInteger eventNum = new AtomicInteger(0);

    /**
     * 日志时间 时间戳
     *
     * @return
     */
    public static synchronized long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static synchronized void addEventNum() {
        eventNum.incrementAndGet();
    }

    /**
     * 目前是逐条操作，所以执行减1
     */
    public static synchronized void decreaseNum() {
        eventNum.decrementAndGet();
    }

    public static synchronized void clearNum() {
        eventNum.set(0);
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
        EventDecorator.addEventNum();
        Logs.d("event-->" + eventNum.get());
        if (EventDecorator.getEventNum() >= GlobalParams.PUSH_CUT_NUMBER) { //当满足连续操作大于100条,就进行上传服务
            Logs.d("event-->满足连续操作大于100,开始上传");
            TrackPushService.getInstance().excutePushEvent();
        }
    }

    /**
     * 返回当前内存中记录的待上传的数据数量
     *
     * @return
     */
    public static int getEventNum() {
        return eventNum.get();
    }

}
