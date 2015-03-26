package com.zncm.mxtg.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zncm.mxtg.R;
import com.zncm.mxtg.uitls.MySp;


public abstract class BaseActivity extends ActionBarActivity {
    protected Context ctx;
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResource() != -1) {
            setContentView(getLayoutResource());
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
//            toolbar.setBackgroundColor(MySp.getTheme());
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_up);
        }
        ctx = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract int getLayoutResource();
}
