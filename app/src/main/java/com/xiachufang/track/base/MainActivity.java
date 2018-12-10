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
    }

    public void addData(View view) {
        for (int i = 0; i < 40; i++) {
            Track.event("https://track.xiachufang.com/action/homepage/impression.gif?origin=android&target_id=1004711&api_key=09844205d1de8adc26110817477a2b70&user_id=131502344&sk=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1NDQ0NTQwODEsImp0aSI6MjI5NDc2ODYsInVpZCI6MTMxNTAyMzQ0LCJvIjoxfQ.lNwOlYYB1P2TK677UciwL-_FsNWOpQd1__xSEtEcXY0&_ts=1544438272&pos=25&url=https%3A%2F%2Fwww.xiachufang.com%2Frecipe%2F1004711%2F&type=recipe&webp=1&device_id=ffffffff-d4df-d499-ffff-ffffb2608abf&nonce=0080FAAD-E21C-4552-BF89-749B10F2BC36&prime=unpurchased&session_count=33&version=356&os_version=25&unique_visitor_id=CC9F5D60-D426-43D3-A830-33F5F7EF7367&api_sign=f06ed6b45abc3e79aaa9f10396bd1cc7&platform=android&section=recommend&");
        }
    }
}
