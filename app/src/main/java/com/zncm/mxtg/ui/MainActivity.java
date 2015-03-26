package com.zncm.mxtg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zncm.mxtg.R;
import com.zncm.mxtg.uitls.MySp;
import com.zncm.mxtg.uitls.XUtil;


public class MainActivity extends BaseActivity {

    Activity ctx;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ctx = this;
//        XUtil.setTextView(ctx, R.id.tvAppInfo,
//                getResources().getString(R.string.app_name) + " " + XUtil.getVersionName(ctx));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                intent = new Intent(ctx, MyActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 500);

        intent = new Intent(ctx, MyActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }
}
