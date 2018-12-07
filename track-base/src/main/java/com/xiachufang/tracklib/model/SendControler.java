package com.xiachufang.tracklib.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * creator huangyong
 * createTime 2018/12/6 下午12:01
 * path com.xiachufang.tracklib.model
 * description:
 */
public class SendControler implements ISendControl{

    //当满足连续操作大于100条,就进行上传服务
    private static final AtomicInteger eventNum = new AtomicInteger(0);

    @Override
    public boolean shouldSend() {

        if (eventNum.get()>=100){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void inCrease() {
        eventNum.incrementAndGet();
    }

    @Override
    public void deCrease() {
        eventNum.decrementAndGet();
    }

    @Override
    public void reset() {
        eventNum.set(0);
    }
}
