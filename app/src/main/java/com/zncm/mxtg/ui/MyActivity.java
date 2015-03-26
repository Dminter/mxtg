package com.zncm.mxtg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;

import com.malinskiy.materialicons.Iconify;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zncm.mxtg.R;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.ft.ProjectFragment;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.CrashHandler;
import com.zncm.mxtg.uitls.RefreshEvent;
import com.zncm.mxtg.uitls.XUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class MyActivity extends BaseActivity {
    protected Activity ctx;
    public TextView mTextView;
    public int type = -1;

    ProjectFragment projectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //时光一去不复返
        getSupportActionBar().setTitle("惜时");

        CrashHandler.init(this);
        CrashHandler.register();
        ctx = this;
//        EventBus.getDefault().register(this);
        XUtil.changeLogFirst(ctx);
        projectFragment = new ProjectFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, projectFragment)
                .commit();

//        Constant.KEY_PARAM_TYPE

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt(Constant.KEY_PARAM_TYPE, -1);
            getSupportActionBar().setTitle("已完成");
        } else {
            toolbar.setNavigationIcon(null);
        }
        mTextView = (TextView) findViewById(R.id.mTextView);
        EventBus.getDefault().register(this);

        //集成检测no
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);

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
            if (projectFragment != null) {
                projectFragment.onRefresh();
            }
        }
    }

}