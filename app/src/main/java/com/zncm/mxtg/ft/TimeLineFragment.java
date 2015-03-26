package com.zncm.mxtg.ft;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxtg.adapter.ProjectAdapter;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.data.TimeData;
import com.zncm.mxtg.ui.DbUtils;
import com.zncm.mxtg.ui.TimeLineActivity;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.XUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TimeLineFragment extends BaseListFragment {
    private ProjectAdapter mAdapter;
    private TimeLineActivity ctx;
    private List<TimeData> datas = null;
    private boolean onLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (TimeLineActivity) getActivity();
        emptyText.setText("暂无时间记录。");
        datas = new ArrayList<TimeData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(int position, PjViewHolder holder) {
                final TimeData timeData = datas.get(position);
                Long start = timeData.getStart_time();
                Long stop = timeData.getStop_time();

                if (start != null && start > 0) {
                    String month, day;
                    month = XUtil.getDateMonth(start);
                    day = XUtil.getDateDay(start);
                    String tmp1 = "";
                    String tmp2 = "";
                    if (position == 0) {
                        tmp1 = "";
                        tmp2 = month + day;
                    } else {
                        Long preTime = datas.get(position - 1).getStart_time();
                        if (preTime != null) {
                            String month_pre =
                                    XUtil.getDateMonth(preTime);
                            String day_pre =
                                    XUtil.getDateDay(preTime);
                            tmp1 = month_pre + day_pre;
                            tmp2 = month + day;
                        }
                    }
                    if (!tmp1.equals(tmp2)) {
                        holder.tvTop.setText(XUtil.getDateMDE(start));
                        holder.tvTop.setVisibility(View.VISIBLE);
                        holder.viewDiv.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvTop.setVisibility(View.GONE);
                        holder.viewDiv.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvTop.setVisibility(View.GONE);
                    holder.viewDiv.setVisibility(View.VISIBLE);
                }


                String stopStr = "";
                String titleStr = "";
                if (stop != null) {
                    stopStr = XUtil.getDateHM(stop);
                }
                if (stop != null && start != null) {
                    titleStr = XUtil.hms(stop - start);
                }


                if (start != null && start > 0) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(XUtil.getDateHM(start) + " - " + stopStr + "   " + titleStr);
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }

                if (XUtil.notEmptyOrNull(timeData.getPj_title())) {
                    holder.tvSpendTime.setVisibility(View.VISIBLE);
                    holder.tvSpendTime.setText(timeData.getPj_title());
                } else {
                    holder.tvSpendTime.setVisibility(View.GONE);
                }
                holder.operate.setVisibility(View.GONE);
            }

        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int curPosition = position - listView.getHeaderViewsCount();
                if (curPosition >= 0 && XUtil.listNotNull(datas)) {
                    if (curPosition >= datas.size()) {
                        return;
                    }
                    TimeData timeData = datas.get(position);
                    if (timeData == null) {
                        return;
                    }
                    if (timeData.getStop_time() == null) {
                        XUtil.tShort("正在进行，不可删除~");
                        return;
                    }
                    delDlg(timeData);

                }

            }
        });
        refresh();
        return view;
    }


    private void delDlg(final TimeData data) {
        new MaterialDialog.Builder(ctx)
                .title("删除")
                .content("确认删除?")
                .theme(Theme.LIGHT)
                .positiveText("删除")
                .negativeText("取消")
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        timeDao.delete(data);
                        refresh();
                    }
                })
                .show();
    }

    private void delAllDlg() {
        new MaterialDialog.Builder(ctx)
                .title("清除所有")
                .content("确认清除?")
                .theme(Theme.LIGHT)
                .positiveText("全部删除")
                .negativeText("取消")
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            DeleteBuilder<TimeData, Integer> deleteBuilder = timeDao.deleteBuilder();
                            deleteBuilder.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refresh();
                    }
                })
                .show();
    }


    private void getData(boolean bFirst) {
        GetData getData = new GetData();
        getData.execute(bFirst);
    }

    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {

            boolean canLoadMore = true;
            try {
                QueryBuilder<TimeData, Integer> builder = timeDao.queryBuilder();
                if (params[0]) {
                    builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
                } else {
                    builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                }
                List<TimeData> list = timeDao.query(builder.prepare());
                if (params[0]) {
                    datas = new ArrayList<TimeData>();
                }
                if (XUtil.listNotNull(list)) {
                    canLoadMore = (list.size() == Constant.MAX_DB_QUERY);
                    datas.addAll(list);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return canLoadMore;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean canLoadMore) {
            super.onPostExecute(canLoadMore);
            mAdapter.setItems(datas);
            swipeLayout.setRefreshing(false);
            onLoading = false;
            getActivity().invalidateOptionsMenu();
            listView.setCanLoadMore(canLoadMore);
            listView.onLoadMoreComplete();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("clear_all")) {
            delAllDlg();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("clear_all").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_clear_all)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        if (onLoading) {
            return;
        }
        refresh();
    }

    private void refresh() {
        onLoading = true;
        swipeLayout.setRefreshing(true);
        getData(true);
    }


    @Override
    public void onLoadMore() {
        getData(false);
    }


}
