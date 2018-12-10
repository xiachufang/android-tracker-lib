package com.xiachufang.tracklib.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.xiachufang.tracklib.TrackManager;

import java.io.IOException;
import java.util.Map;


/**
 * creator huangyong
 * createTime 2018/11/29 上午10:21
 * path com.xiachufang.tracklib.net
 * description: 请求分发，如果没有传入自定义的httpmanager,则使用内部的发送机制，即httpurlConnection
 */
public class TrackHurlStack extends HurlStack {


    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        IHttpManager httpManager = TrackManager.getHttpManager();
        if (httpManager!=null){
            HttpResponse httpResponse = httpManager.doGet((StaticRequest) request);
            return httpResponse;
        }else {
            return super.executeRequest(request, additionalHeaders);
        }
    }



}
