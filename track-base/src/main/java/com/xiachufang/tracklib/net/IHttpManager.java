package com.xiachufang.tracklib.net;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.HttpResponse;

import java.io.IOException;

/**
 * 网络请求接口
 */
public interface IHttpManager{


    /**
     * 同步 getInstance
     * @param request
     * @return response code
     */
    HttpResponse doGet(StaticRequest request) throws IOException, AuthFailureError;

    /**
     * 网络请求回调
     */
    interface Callback {
        /**
         * 结果回调
         *
         * @param id 数据ID
         */
        void onResponse(int id);

        /**
         * 错误回调
         *
         * @param id 数据ID
         */
        void onError(int id);
    }
}
