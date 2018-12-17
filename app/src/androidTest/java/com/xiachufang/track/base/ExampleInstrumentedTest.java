package com.xiachufang.track.base;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import com.xiachufang.tracklib.controler.Track;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.db.helper.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.xiachufang.track.base", appContext.getPackageName());

    }


    @Before
    public void addData(){
        for (int i = 0; i < 10000; i++) {
            Track.event("http://123.207.150.253/testTrack.php?trackUrl=http://www.tessfdst4.html");
        }
        Log.e("testSendTime","sendstart");
    }

    @After
    public void getData(){
        List<TrackData> trackData = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext()).trackDao().getAll();
        Log.e("trackdatatest",trackData.size()+"条");
    }
}
