package com.xiachufang.tracklib.net;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.db.TrackDBManager;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.model.EventDecorator;
import com.xiachufang.tracklib.services.TrackPushService;
import com.xiachufang.tracklib.util.GlobalParams;
import com.xiachufang.tracklib.util.Logs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * creator huangyong
 * createTime 2018/11/29 上午10:31
 * path com.xiachufang.tracklib.net
 * description:
 */
public class TrackHttpManager {


    private static volatile RequestQueue requestQueue;

    private static volatile TrackHttpManager volleyManager;

    private static final AtomicInteger waitNum = new AtomicInteger(0);
    private int mTimeOutMilliSecs=3;

    public static RequestQueue statistics() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(TrackManager.getContext(), new TrackHurlStack());
        }
        return requestQueue;
    }

    static IHttpManager.Callback callback = new IHttpManager.Callback() {
        @Override
        public void onResponse(int id) {
            TrackDBManager.deleteEventByDataId(TrackManager.getContext(), id);
            EventDecorator.decreaseNum();
            //是否存在被拒绝的任务，立即读取数据
            if (requestQueue.getCurrentQueueSize() == 0 && waitNum.get() > 0) {
                waitNum.decrementAndGet();
                TrackPushService.getInstance().excutePushEvent();
                Log.e("currentthread", "曾有新数据被拒绝，重新入队");
            }
        }

        @Override
        public void onError(int id) {
            Log.e("trackurlstack", " request callback fail" + id);
            EventDecorator.decreaseNum();

            //是否存在被拒绝的任务，立即读取数据
            if (requestQueue.getCurrentQueueSize() == 0 && waitNum.get() > 0) {
                waitNum.decrementAndGet();
                TrackPushService.getInstance().excutePushEvent();
                Log.e("currentthread", "曾有新数据被拒绝，重新入队");
            }
            Logs.d("refresh");
        }
    };

    private TrackHttpManager(RequestQueue statistics) {
        requestQueue = statistics;
    }


    public static TrackHttpManager get() {

        if (volleyManager == null) {
            synchronized (TrackHttpManager.class) {
                volleyManager = new TrackHttpManager(statistics());
            }
        }
        return volleyManager;
    }

    /**
     * 如果外部没有传入httpManager,默认采用内部发送，如果传入，以外部发送
     *
     * @param list
     */
    public void send(List<TrackData> list) {
        for (TrackData trackData : list) {
            StaticRequest request = createRequest(trackData, callback);
            requestQueue.add(request);
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private StaticRequest createRequest(TrackData trackData, IHttpManager.Callback callback) {
        Gson gson = new Gson();
        HashMap<String,String> trackMap = gson.fromJson(trackData.getTrackData(),HashMap.class);
        String trackUrl = trackMap.get(GlobalParams.URL_KEY);
        String trackParamsMap = trackMap.get(GlobalParams.PARAMS_MAP_KEY);
        HashMap<String,String> requestMap = gson.fromJson(trackParamsMap,HashMap.class);
        return buildRequest(trackUrl, requestMap, trackData.getId(), callback);
    }

    //TODO 发布时要换正确的请求地址 trackingUrl
    private StaticRequest buildRequest(String trackingUrl, Map trackParamsMap, int id, IHttpManager.Callback callback) {

        Log.e("requestUrlis", trackingUrl);
        StaticRequest request = new StaticRequest(TrackManager.METHOD_GET, "http://123.207.150.253/ygcms/app/update.json", trackParamsMap, callback, id);
        request.setShouldCache(false);
        if (this.mTimeOutMilliSecs >= 3) {
            request.setRetryPolicy(new DefaultRetryPolicy(this.mTimeOutMilliSecs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
        if (TrackManager.getHeadrConfig() != null) {
            return TrackManager.getHeadrConfig().getHeaders(request);
        }
        return request;
    }


    public synchronized void increaseRequestWaitNum() {
        waitNum.incrementAndGet();
    }

    public synchronized void clearRequetWaitNum() {
        waitNum.set(0);
    }
}
