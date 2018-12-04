package com.xiachufang.tracklib.db.helper;

import android.content.Context;

import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.db.dao.TrackDao;

import java.util.List;

/**
 * creator huangyong
 * createTime 2018/11/27 下午3:11
 * path com.xiachufang.tracklib.db.helper
 * description:
 */
public class TrackDbHelper {


    public static void saveTrack(Context context, TrackData trackData) {
        AppDatabase.getInstance(context).trackDao().insert(trackData);
    }

    public static List getTrack(Context context, long cut_point_date) {
        TrackDao trackDao = AppDatabase.getInstance(context).trackDao();
        List<TrackData> result = trackDao.getTrackDataByTime(cut_point_date);

        return result;
    }

    public static synchronized void deleteTrackDataById(Context context, int dataId) {

        List<TrackData> trackData = AppDatabase.getInstance(context).trackDao().getTrackDataById(dataId);
        if (trackData!=null&&trackData.size()>0){
            AppDatabase.getInstance(context).trackDao().delete(trackData.get(0));
        }

    }
}
