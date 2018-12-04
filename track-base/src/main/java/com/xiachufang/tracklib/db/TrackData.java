package com.xiachufang.tracklib.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * creator huangyong
 * creator huangyong
 * createTime 2018/11/13 下午5:21
 * path com.xiachufang.tracklib.db
 * description:
 *
 */

@Entity(tableName = TrackData.TABLE_NAME,indices = {@Index("timestamp")})
public class TrackData {

    public static final String TABLE_NAME = "t_track";

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "track_data")
    public String track_data;

    @ColumnInfo(name = "track_host")
    public String track_host;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    public String getTrack_host() {
        return track_host;
    }

    public void setTrack_host(String track_host) {
        this.track_host = track_host;
    }

    public long getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timestamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackData() {
        return track_data;
    }

    public void setTrackData(String track_params) {
        this.track_data = track_params;
    }


}
