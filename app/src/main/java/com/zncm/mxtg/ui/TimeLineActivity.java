package com.zncm.mxtg.ui;

import android.os.Bundle;

import com.zncm.mxtg.R;
import com.zncm.mxtg.ft.PjAddFragment;
import com.zncm.mxtg.ft.TimeLineFragment;


public class TimeLineActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("时光机");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new TimeLineFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
