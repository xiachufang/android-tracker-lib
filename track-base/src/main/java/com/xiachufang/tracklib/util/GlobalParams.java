package com.xiachufang.tracklib.util;

/**
 * creator huangyong
 * createTime 2018/11/13 上午10:03
 * path com.xiachufang.tracklib
 * description:
 */
public class GlobalParams {

    public static final String TAG = "reportTrack";

    /**
     * 记录到达xx条,主动进行上传,默认100
     */
    public static int PUSH_CUT_NUMBER = 100;

    /**
     * 上传间隔事件 分钟, 默认1分钟
     */
    public static double PUSH_CUT_TIMER_INTERVAL = 1;


    //是否是开发模式
    public static boolean DEVELOP_MODE = true;
    //开启一切统计逻辑
    public static boolean SWITCH_ON = true;

}
