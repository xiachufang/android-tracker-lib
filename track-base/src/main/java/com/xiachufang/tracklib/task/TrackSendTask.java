package com.xiachufang.tracklib.task;

import android.content.Context;

import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.db.TrackDBManager;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.model.EventDecorator;
import com.xiachufang.tracklib.net.TrackHttpManager;
import com.xiachufang.tracklib.util.Logs;
import com.xiachufang.tracklib.util.NetworkUtil;

import java.util.List;

/**
 * creator huangyong
 * createTime 2018/11/14 上午11:06
 * path com.xiachufang.tracklib.controler
 * description: 发送的任务
 */
public class TrackSendTask {


    //校验数据库最新数据时间戳
    private static volatile long cut_point_date = 0;


    public static synchronized void pushEvent() {


        Context context = TrackManager.getContext();


        if (context == null) {
            return;
        }
        //1.判断网络状况是否良好
        if (!NetworkUtil.isNetworkAvailable(context)) {
            return;
        }
        //2.判断当前是否有任务正在进行，如果有，不进行数据读。
        if (TrackHttpManager.get().getRequestQueue().getCurrentQueueSize()!=0){
            TrackHttpManager.get().increaseRequestWaitNum();
            return;
        }

        //3.获取当前时间.
        cut_point_date = EventDecorator.getTimeStamp();

        //4.获取小于当前时间的数据 集合`push_list`.
        List<TrackData> list = TrackDBManager.getEventListByDate(context,cut_point_date);

        //5.根据list大小创建一个int值计数器，对这个唯一队列计数，

        if (list == null || list.size() == 0) {
            Logs.d("没有需要发送的统计数据");
            TrackManager.getSendControler().reset();
            TrackHttpManager.get().clearRequetWaitNum();
            return;
        }
        //6.发送数据
        TrackHttpManager.get().send(list);


    }




}
