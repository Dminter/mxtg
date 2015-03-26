package com.zncm.mxtg.ft;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.zncm.mxtg.R;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.data.TimeData;
import com.zncm.mxtg.ui.MyApplication;
import com.zncm.mxtg.uitls.MySp;
import com.zncm.mxtg.uitls.db.DatabaseHelper;
import com.zncm.mxtg.view.loadmore.LoadMoreListView;


public abstract class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    protected SwipeRefreshLayout swipeLayout;
    protected LoadMoreListView listView;
    protected View view;
    protected LayoutInflater mInflater;
    protected RuntimeExceptionDao<ProjectData, Integer> projectDao;
    protected RuntimeExceptionDao<TimeData, Integer> timeDao;
    private DatabaseHelper databaseHelper = null;

    protected TextView emptyText;

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        view = inflater.inflate(R.layout.fragment_base, null);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
//        swipeLayout.setEnabled(false);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(
                MySp.getTheme()
        );
        listView = (LoadMoreListView) view.findViewById(R.id.listView);
        listView.setOnLoadMoreListener(this);


        emptyText = (TextView) view.findViewById(R.id.emptyText);
        listView.setEmptyView(emptyText);


        if (projectDao == null) {
            projectDao = getHelper().getPjDao();
        }
        if (timeDao == null) {
            timeDao = getHelper().getTdDao();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
