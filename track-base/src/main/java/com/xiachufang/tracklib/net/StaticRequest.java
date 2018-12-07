package com.xiachufang.tracklib.net;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by michael on 15/9/25.
 */
public class StaticRequest<T> extends Request<T> {
    public static final int METHOD_GET = 0;
    private Map<String, String> mHeaders;
    private Map<String, Object> mRequestParams;
    private IHttpManager.Callback httpCallback;
    private MultipartEntity mMultipartEntity;
    private int requestId;

    public StaticRequest(int method, String url, Map<String, Object> reqParams, IHttpManager.Callback responseListener, int id) {
        super(method, url, null);
        this.httpCallback = responseListener;
        this.requestId = id;
        if (reqParams != null) {
            setRequestParams(reqParams);
        }

    }



    @Override
    public Map<String, String> getHeaders() {
        if (mHeaders == null ) {
            mHeaders = new HashMap<String, String>();
            mHeaders.put("User-Agent", "testUseragent");
        }
        return mHeaders;
    }

    public void addHeader(String name, String value) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, String> headers = getHeaders();
        headers.put(name, value);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        mMultipartEntity = buildMultiPartEntity(mRequestParams);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mMultipartEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String rawString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
            T result = null;
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T model) {
        if (httpCallback != null && requestId != 0) {
            httpCallback.onResponse(requestId);
            httpCallback = null;
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (httpCallback != null && requestId != 0) {
            httpCallback.onError(requestId);
            httpCallback = null;
        }
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        mRequestParams = requestParams;
    }

    private MultipartEntity buildMultiPartEntity(Map<String, Object> reqParams) {
        MultipartEntity entity = new MultipartEntity();
        if (reqParams != null && !reqParams.isEmpty()) {
            for (String key : reqParams.keySet()) {
                Object value = reqParams.get(key);
                if (value instanceof File) {
                    entity.addPart(key, new FileBody((File) value));
                } else {
                    try {
                        entity.addPart(key, new StringBody(String.valueOf(value), Charset.forName("UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return entity;
    }

    @Override
    public String getBodyContentType() {
        return mMultipartEntity.getContentType().getValue();
    }

    @Override
    public void cancel() {
        super.cancel();
        httpCallback = null;
    }

}
