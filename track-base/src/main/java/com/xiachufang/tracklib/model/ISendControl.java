package com.xiachufang.tracklib.model;

/**
 * creator huangyong
 * createTime 2018/12/6 下午12:01
 * path com.xiachufang.tracklib.model
 * description:
 */
public interface ISendControl {

    boolean shouldSend();

    void inCrease();

    void deCrease();

    void reset();
}
