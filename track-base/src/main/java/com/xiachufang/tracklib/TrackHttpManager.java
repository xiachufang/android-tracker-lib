package com.xiachufang.tracklib;

import android.net.Uri;
import android.util.ArrayMap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xiachufang.tracklib.db.TrackDBManager;
import com.xiachufang.tracklib.db.TrackData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * creator huangyong
 * createTime 2018/11/29 上午10:31
 * path com.xiachufang.tracklib.net
 * description:
 */
class TrackHttpManager {


    private static volatile RequestQueue requestQueue;

    private static volatile TrackHttpManager volleyManager;

    private static final AtomicInteger waitNum = new AtomicInteger(0);

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
            TrackManager.getSendControler().deCrease();
            if (requestQueue.getCurrentQueueSize() == 0 && waitNum.get() > 0) {
                waitNum.decrementAndGet();
                TrackPushService.getInstance().excutePushEvent();

            }
        }

        @Override
        public void onError(int id) {
            TrackManager.getSendControler().deCrease();
            if (requestQueue.getCurrentQueueSize() == 0 && waitNum.get() > 0) {
                waitNum.decrementAndGet();
                TrackPushService.getInstance().excutePushEvent();
            }
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
        for (final TrackData trackData : list) {
            StaticRequest request = createRequest(trackData, callback);
            requestQueue.add(request);
        }
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private StaticRequest createRequest(TrackData trackData, IHttpManager.Callback callback) {
        String trackingUrl = trackData.getTrackData();

        Uri androidUri = Uri.parse(trackingUrl);
        List<String> pathSegments = androidUri.getPathSegments();
        StringBuilder pathBuilder = new StringBuilder();
        if (pathSegments != null) {
            for (int i = 0; i < pathSegments.size(); i++) {
                pathBuilder.append(pathSegments.get(i));
                if (i < pathSegments.size() - 1) {
                    pathBuilder.append('/');
                }
            }
        }
        Set<String> queryParameterNames = androidUri.getQueryParameterNames();
        Map<String, Object> parameterMap = new ArrayMap<>();
        if (queryParameterNames != null && queryParameterNames.size() > 0) {
            for (String queryParameterName : queryParameterNames) {
                String parameterValue = androidUri.getQueryParameter(queryParameterName);
                if (parameterValue == null) {
                    continue;
                }
                parameterMap.put(queryParameterName, parameterValue);
            }
        }
        return buildRequest(trackingUrl, parameterMap, trackData.getId(), callback);
    }

    private StaticRequest buildRequest(String trackingUrl, Map trackParamsMap, int id, IHttpManager.Callback callback) {
        StaticRequest request;
        if (trackingUrl.endsWith("&")){
            trackingUrl+="id="+id;
        }else {
            trackingUrl+="&id="+id;
        }
        request = new StaticRequest(StaticRequest.METHOD_GET, trackingUrl, trackParamsMap,id, callback);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy());
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
