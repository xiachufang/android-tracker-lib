package com.xiachufang.tracklib;


/**
 * creator huangyong
 * createTime 2018/12/3 下午5:08
 * path com.xiachufang.tracklib.net
 * description:
 */
public interface IHeaderConfig {

    StaticRequest getHeaders(StaticRequest request);

    String getUserAgent();
}
