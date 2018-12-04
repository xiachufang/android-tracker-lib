package com.xiachufang.tracklib.services;

import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.task.TrackPushTask;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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

        Observable<Long> interval = Observable.interval(0, 60, TimeUnit.SECONDS);
        final Consumer<Long> subscriber = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (SWITCH_ON){
                    TrackPushTask.pushEvent();
                }
            }
        };
        subscribe = interval.subscribe(subscriber);

    }


    /**
     * 主动调用push操作,运行在主线程中.
     */
    public void excutePushEvent() {

        TrackPoolExecutor.getInstance().execute(new FutureTask<Object>(new Runnable() {
            @Override
            public void run() {
                TrackPushTask.pushEvent();
            }
        }, null));
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
            synchronized (TrackManager.class) {
                if (pushService == null) {
                    pushService = new TrackPushService();
                }
            }
        }
        return pushService;
    }

}
