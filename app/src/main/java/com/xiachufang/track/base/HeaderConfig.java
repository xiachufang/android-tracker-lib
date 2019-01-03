package com.xiachufang.track.base;


import com.xiachufang.tracklib.IHeaderConfig;
import com.xiachufang.tracklib.StaticRequest;

/**
 * creator huangyong
 * createTime 2018/12/3 下午5:24
 * path com.xiachufang.track
 * description:
 */
public class HeaderConfig implements IHeaderConfig {
    @Override
    public StaticRequest getHeaders(StaticRequest request) {
       return request;
    }

    @Override
    public String getUserAgent() {
        return "useraget";
    }
}
