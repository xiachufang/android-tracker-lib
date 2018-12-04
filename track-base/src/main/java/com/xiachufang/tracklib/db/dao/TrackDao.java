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
 * description:tag=0为默认状态，1为正在发送，2为发送失败
 * 每次都只取0和2的数据发送，取的同时改状态为1，。暂定取100条
 */
@Dao
public interface TrackDao {

    @Query("SELECT * FROM T_TRACK WHERE  timeStamp<:cut_point_date LIMIT 100")
    List<TrackData> getTrackDataByTime(long cut_point_date);


    @Query("SELECT * FROM T_TRACK WHERE id =:trackId")
    List<TrackData> getTrackDataById(int trackId);

    @Insert
    public void insert(TrackData trackData);

    @Delete
    public void delete(TrackData trackData);

    @Query("SELECT * FROM T_TRACK")
    List<TrackData> getAll();

}
