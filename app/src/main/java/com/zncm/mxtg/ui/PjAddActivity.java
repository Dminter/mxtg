package com.zncm.mxtg.ui;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.zncm.mxtg.R;
import com.zncm.mxtg.ft.PjAddFragment;


public class PjAddActivity extends BaseActivity {
    PjAddFragment pjAddFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pjAddFragment = new PjAddFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, pjAddFragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (pjAddFragment != null) {
            pjAddFragment.updateBack();
        }

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
}
