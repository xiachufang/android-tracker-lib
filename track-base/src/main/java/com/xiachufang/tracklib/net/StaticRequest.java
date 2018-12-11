package com.xiachufang.tracklib.net;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.util.Map;


/**
 * creator huangyong
 * createTime 2018/12/11 上午10:29
 * path com.xiachufang.tracklib.net
 * description:自定义的request
 */
public class StaticRequest<T> extends Request<T> {

    public static final int METHOD_GET = 0;
    private int id;
    private final IHttpManager.Callback listener;
    private final Map<String, String> params;
    public StaticRequest(int method, String url, Map trackParamsMap, int id, IHttpManager.Callback listener) {
        super(method, url, null);
        this.listener = listener;
        this.id = id;
        this.params = trackParamsMap;
    }

    /**
     * 由于不需要解析返回的结果，只获取是否提交成功的状态，所以这里没有处理
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    null, HttpHeaderParser.parseCacheHeaders(response));
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        try {
            listener.onResponse(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        try {
            listener.onError(id);
        }catch (Exception e){

        }
    }
}
