package com.zncm.mxtg.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zncm.mxtg.R;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.ft.ProjectFragment;
import com.zncm.mxtg.ft.ReviewFragment;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.RefreshEvent;

import java.io.Serializable;

import de.greenrobot.event.EventBus;


public class ReviewActivity extends BaseActivity {
    protected Activity ctx;
    public TextView mTextView;
    ReviewFragment reviewFragment;
    public  boolean bDay = true;
public ProjectData pj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        ctx = this;
        reviewFragment = new ReviewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, reviewFragment)
                .commit();
        mTextView = (TextView) findViewById(R.id.mTextView);


        if (getIntent().getExtras()!=null){
            bDay = getIntent().getBooleanExtra(Constant.KEY_PARAM_BOOLEAN, true);
            Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
            pj = (ProjectData) dataParam;
        }

        EventBus.getDefault().register(this);
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
        return R.layout.activity_my;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.PROJECT.getValue()) {
            if (reviewFragment != null) {
                reviewFragment.onRefresh();
            }
        }
    }

}