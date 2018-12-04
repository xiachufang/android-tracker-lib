package com.xiachufang.tracklib.db;

import android.content.Context;

import com.xiachufang.tracklib.TrackManager;
import com.xiachufang.tracklib.db.helper.TrackDbHelper;

import java.util.List;

/**
 * creator huangyong
 * createTime 2018/11/14 上午11:43
 * path com.xiachufang.tracklib.db
 * description: 操作数据中介类
 */
public class TrackDBManager {


    public static List getEventListByDate(Context context, long cut_point_date) {
       return TrackDbHelper.getTrack(context,cut_point_date);
    }

    public static void deleteEventByDataId(Context context, int id) {
        TrackDbHelper.deleteTrackDataById(context,id);
    }

    public static void addEventData(TrackData trackData) {
        TrackDbHelper.saveTrack(TrackManager.getContext(),trackData);
    }
}
