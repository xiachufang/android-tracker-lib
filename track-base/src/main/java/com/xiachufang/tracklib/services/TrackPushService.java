package com.xiachufang.tracklib.services;

import android.util.Log;

import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.db.helper.AppDatabase;
import com.xiachufang.tracklib.task.TrackSendTask;
import com.xiachufang.tracklib.util.GlobalParams;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.xiachufang.tracklib.util.GlobalParams.SWITCH_ON;


/**
 * *  * 后台服务,app启动时开启.
 * Created by chenchangjun on 18/2/24.
 */

public class TrackPushService {

    private static TrackPushService pushService;

    private Disposable subscribe;

    private TrackPushService() {
        init();
    }

    private void init() {

        Observable<Long> interval = Observable.interval(0, (long) GlobalParams.PUSH_CUT_TIMER_INTERVAL, TimeUnit.MINUTES);
        final Consumer<Long> subscriber = new Consumer<Long>() {
            @Override
            public void accept(Long aLong){
                if (SWITCH_ON){
                    TrackSendTask.pushEvent();
                }
            }
        };

        subscribe = interval.subscribeOn(Schedulers.newThread()).subscribe(subscriber);

    }


    /**
     * 主动调用push操作,运行在主线程中.
     */
    public void excutePushEvent() {

        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() {
                TrackSendTask.pushEvent();
                return new Object();
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();


        //debug
        if (GlobalParams.DEVELOP_MODE){
            List<TrackData> trackData = AppDatabase.getInstance(TrackManager.getContext()).trackDao().getAll();
            StringBuffer buffer = new StringBuffer();
            if (trackData!=null&&trackData.size()>0){
                for (int i = 0; i < trackData.size(); i++) {
                    buffer.append(trackData.get(i).getTrackData()).append("-timestamp-").append(trackData.get(i).getTimeStamp()).append("\n");
                }
            }
            Log.e("debug-statistic",buffer.toString());
        }
    }


    /**
     * 停止 推送 服务
     */
    public void stopEventService() {
        if (subscribe!= null&&!subscribe.isDisposed()) {
            subscribe.dispose();
        }

    }

    /**
     * 启动 定时推送服务
     */
    public static void startService() {
        if (pushService != null) {
            getInstance().init();
        } else {
            getInstance();
        }

    }

    /**
     * 获取 推送服务 单例
     *
     * @return
     */
    public static TrackPushService getInstance() {

        if (pushService == null) {
            synchronized (TrackPushService.class) {
                if (pushService == null) {
                    pushService = new TrackPushService();
                }
            }
        }
        return pushService;
    }

}
