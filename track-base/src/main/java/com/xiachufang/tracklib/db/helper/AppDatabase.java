package com.xiachufang.tracklib.db.helper;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.db.dao.TrackDao;

/**
 * creator huangyong
 * createTime 2018/11/27 下午2:42
 * path com.xiachufang.tracklib.db.helper
 * description:
 */
@Database(entities = {TrackData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TABLE_NAME = "com_xiachufang_tracklib.db";

    public abstract TrackDao trackDao();

    private static AppDatabase instance;
    private static final Object sLock = new Object();
    public static synchronized AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (instance == null) {
                instance =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.TABLE_NAME)
                                .allowMainThreadQueries()
                                .build();
            }
            return instance;
        }

    }
}
