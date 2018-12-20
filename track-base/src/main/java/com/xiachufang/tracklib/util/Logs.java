package com.xiachufang.tracklib.util;


/**
 * creator huangyong
 * createTime 2018/11/14 下午3:55
 * path com.xiachufang.tracklib.util
 * description:
 */
public class Logs {


    public static void d(String debug){
        if (GlobalParams.DEVELOP_MODE){
            android.util.Log.d(GlobalParams.TAG,debug);
        }
    }
}
