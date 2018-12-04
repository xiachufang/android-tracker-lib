package com.xiachufang.tracklib.controler;


import com.xiachufang.tracklib.services.TrackPoolExecutor;
import com.xiachufang.tracklib.task.TrackSaveTask;

import java.util.concurrent.FutureTask;


/**
 * creator huangyong
 * createTime 2018/11/14 上午11:43
 * path com.xiachufang.tracklib.db
 * description: 埋点入口，保存数据控制类
 */
public final class Track {

    /**
     * 打点入口
     */
    public static void event( String trackUrl) {
        try {
            TrackSaveTask eventTask =new TrackSaveTask(trackUrl);
            TrackPoolExecutor.getInstance().execute(new FutureTask<Object>(eventTask,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
