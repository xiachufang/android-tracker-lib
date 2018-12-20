package com.xiachufang.track.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiachufang.tracklib.controler.Track;
import com.xiachufang.tracklib.db.TrackData;
import com.xiachufang.tracklib.db.helper.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvnum = findViewById(R.id.tvnum);
    }

    public void getAlldata(View view) {
        List<TrackData> trackData = AppDatabase.getInstance(this).trackDao().getAll();
          Log.e("trackdatatest",trackData.size()+"条");
        tvnum.setText("当前有"+trackData.size()+"条数据");
        if (trackData.size()==0){
            Log.e("testSendTime","sendend");
        }
    }

    public void addData(View view) {
        for (int i = 0; i < 100; i++) {
            Track.event("http://123.207.150.253/testTrack.php?trackUrl=http://www.tessfdst4.html");
        }
        Log.e("testSendTime","sendstart");
    }
}
