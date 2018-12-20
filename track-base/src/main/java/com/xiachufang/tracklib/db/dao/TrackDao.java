package com.xiachufang.tracklib.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.xiachufang.tracklib.db.TrackData;

import java.util.List;

/**
 * creator huangyong
 * createTime 2018/11/27 下午2:13
 * path com.xiachufang.tracklib.db.dao
 */
@Dao
public interface TrackDao {

    @Query("SELECT * FROM T_TRACK WHERE  timeStamp<:cut_point_date LIMIT 100")
    List<TrackData> getTrackDataByTime(long cut_point_date);


    @Query("SELECT * FROM T_TRACK WHERE id =:trackId")
    List<TrackData> getTrackDataById(int trackId);

    @Insert
    void insert(TrackData trackData);

    @Delete
    void delete(TrackData trackData);

    @Query("SELECT * FROM T_TRACK")
    List<TrackData> getAll();

}
