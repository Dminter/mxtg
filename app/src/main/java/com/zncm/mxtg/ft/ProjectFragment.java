package com.zncm.mxtg.ft;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.j256.ormlite.stmt.QueryBuilder;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxtg.R;
import com.zncm.mxtg.adapter.ProjectAdapter;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.ui.DbUtils;
import com.zncm.mxtg.ui.MyActivity;
import com.zncm.mxtg.ui.PjAddActivity;
import com.zncm.mxtg.ui.ReviewActivity;
import com.zncm.mxtg.ui.TimeLineActivity;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.MySp;
import com.zncm.mxtg.uitls.XUtil;

import java.util.ArrayList;
import java.util.List;


public class ProjectFragment extends BaseListFragment {
    private ProjectAdapter mAdapter;
    private MyActivity ctx;
    private List<ProjectData> datas = null;
    private boolean onLoading = false;
    Handler handler = new Handler();
    Long timeGo;
    List<ProjectData> pjList;
    int sort = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (MyActivity) getActivity();
        emptyText.setText("点击右上角+号，开启惜时之旅！");

        sort = MySp.getSort();

        datas = new ArrayList<ProjectData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(int position, final PjViewHolder holder) {
                final ProjectData pj = datas.get(position);

                if (pj == null) {
                    return;
                }


                if (ctx.type == EnumData.StatusEnum.FINISH.getValue()) {
                    Long stop = pj.getStop_time();
//                    if (stop != null && stop > 0) {
//                        holder.tvStopTime.setVisibility(View.VISIBLE);
//                        holder.tvStopTime.setText(XUtil.getDateMDEHM(stop));
//                    } else {
//                        holder.tvStopTime.setVisibility(View.GONE);
//                    }
                    if (pj.getFrequency() == EnumData.FrequencyEnum.NORMAL.getValue()) {
                        holder.tvTag.setText("日常");
                        holder.tvTag.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvTag.setVisibility(View.GONE);
                    }

                } else {
                    holder.tvTag.setVisibility(View.GONE);
//                    holder.tvStopTime.setVisibility(View.GONE);
                }


                if (XUtil.notEmptyOrNull(pj.getTitle())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);

                    holder.tvTitle.setText(pj.getTitle());
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }


                holder.tvSpendTime.setVisibility(View.VISIBLE);


                Long expect = pj.getExpect_time();
                Long spend = pj.getSpend_time();


                String show;

                if (spend != null && spend > 0) {
                    if (expect != null && expect > 0) {
                        if (spend >= expect) {
                            show = "√";
                        } else {
                            show = XUtil.hms(expect);
                        }
                        holder.tvSpendTime.setText(XUtil.hms(spend) + " -> " + show);
                    } else {
                        holder.tvSpendTime.setText(XUtil.hms(spend));
                    }

                } else {
                    holder.tvSpendTime.setText("");
                }


                if (pj.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.accent_light));
                } else {
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.white));
                }


                float progress = 0f;
                if (expect != null && expect > 0 && spend != null && spend > 0) {
                    progress = spend * 100 / expect;
                    if (progress <= 20) {
                        holder.bar.setReachedBarColor(getResources().getColor(R.color.color1));
                    } else if (progress <= 40 && progress > 20) {
                        holder.bar.setReachedBarColor(getResources().getColor(R.color.color2));
                    } else if (progress <= 60 && progress > 40) {
                        holder.bar.setReachedBarColor(getResources().getColor(R.color.color3));
                    } else if (progress <= 80 && progress > 60) {
                        holder.bar.setReachedBarColor(getResources().getColor(R.color.color4));
                    }
//                    else if (progress == 100) {
//                        holder.bar.setReachedBarColor(getResources().getColor(R.color.color5));
//                    }
                    if (progress < 100) {
                        holder.bar.setProgress((int) progress);
                        holder.bar.setVisibility(View.VISIBLE);
                    } else {
                        holder.bar.setVisibility(View.GONE);
                    }
                } else {
                    holder.bar.setVisibility(View.GONE);
                }


//                if (ctx.type == EnumData.StatusEnum.FINISH.getValue()) {
//                    holder.finish.setText("{md-refresh}");
//                } else {
//                    holder.finish.setText("{md-done}");
//                }
//
//
//                holder.finish.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pjFinish(pj);
//                    }
//                });
                holder.operate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initOperate(pj);
                    }
                });
//
//
//                holder.edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ctx, PjAddActivity.class);
//                        intent.putExtra(Constant.KEY_PARAM_DATA, pj);
//                        startActivity(intent);
//                    }
//                });
//
//
//                holder.delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        delDlg(pj);
//                    }
//                });


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
                    ProjectData projectData = datas.get(curPosition);
                    if (projectData == null) {
                        return;
                    }
                    if (projectData.getStatus() == EnumData.StatusEnum.ON.getValue() && timeGo != null) {
                        return;
                    }
                    if (ctx.type != -1) {
                        return;
                    }
                    DbUtils.timeSum(projectData);
                    refresh();
                }

            }
        });


        refresh();
        return view;
    }

    private void pjFinish(ProjectData pj) {
        if (ctx.type == EnumData.StatusEnum.FINISH.getValue()) {
            pj.setStatus(EnumData.StatusEnum.OFF.getValue());
        } else {
            pj.setStatus(EnumData.StatusEnum.FINISH.getValue());
        }

        pj.setStop_time(XUtil.getLongTime());
        pj.setModify_time(XUtil.getLongTime());
        projectDao.update(pj);

        if (ctx.type != EnumData.StatusEnum.FINISH.getValue()) {
            DbUtils.stopAll(pj);
        }
        refresh();
    }

    private void delDlg(final ProjectData data) {
        new MaterialDialog.Builder(ctx)
                .title("删除")
                .content("确认删除?")
                .theme(Theme.LIGHT)
                .positiveText("删除")
                .negativeText("取消")
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        data.setStatus(EnumData.StatusEnum.DEL.getValue());
                        data.setModify_time(System.currentTimeMillis());
                        projectDao.update(data);
                        DbUtils.delTimeByPj(data.getId());
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
                QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
                if (ctx.type == -1) {
                    builder.where().notIn("status", EnumData.StatusEnum.DEL.getValue(), EnumData.StatusEnum.FINISH.getValue());
                } else {
                    builder.where().eq("status", ctx.type);
                }


                if (ctx.type == EnumData.StatusEnum.FINISH.getValue()) {
                    builder.orderBy("stop_time", false);
                } else {
                    if (sort == EnumData.SortEnum.TIME_DOWN.getValue()) {
                        builder.orderBy("time", false);
                    } else if (sort == EnumData.SortEnum.TIME_UP.getValue()) {
                        builder.orderBy("time", true);
                    } else if (sort == EnumData.SortEnum.SPEND_DOWN.getValue()) {
                        builder.orderBy("spend_time", false);
                    } else if (sort == EnumData.SortEnum.SPEND_UP.getValue()) {
                        builder.orderBy("spend_time", true);
                    }
                }


                if (params[0]) {

                    builder.limit(Constant.MAX_DB_QUERY);
                } else {
                    builder.limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                }
                List<ProjectData> list = projectDao.query(builder.prepare());
                if (params[0]) {
                    datas = new ArrayList<ProjectData>();
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

        if (item.getTitle().equals("add")) {
            Intent intent = new Intent(ctx, PjAddActivity.class);
            startActivity(intent);
        } else if (item.getTitle().equals("alarm_off")) {
            DbUtils.stopAll(null);
            refresh();
        }

//        else if (item.getTitle().equals("time_line")) {
//            Intent intent = new Intent(ctx, TimeLineActivity.class);
//            startActivity(intent);
//        } else if (item.getTitle().equals("more")) {
//            Intent intent = new Intent(ctx, MyActivity.class);
//            intent.putExtra(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.FINISH.getValue());
//            startActivity(intent);
//        }


        switch (item.getItemId()) {
            case 1:


                Intent intent = new Intent(ctx, TimeLineActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(ctx, ReviewActivity.class);
//                intent.putExtra(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.FINISH.getValue());
                startActivity(intent);
                break;

            case 3:
                initProject();
                break;
            case 4:
                initSort();

                break;
            case 5:
                ctx.finish();
                break;
            case 6:
                intent = new Intent(ctx, ReviewActivity.class);
                intent.putExtra(Constant.KEY_PARAM_DATA, DbUtils.getLastFrequencyPj());
                intent.putExtra(Constant.KEY_PARAM_BOOLEAN, false);
                startActivity(intent);
                break;
            case 7:
                XUtil.changeLog(ctx);
                break;
            case 8:
                Uri uri = Uri.parse(Constant.FEEDBACK_URL);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void initSort() {

        final MaterialDialog md = new MaterialDialog.Builder(ctx)
                .positiveText("取消")
                .items(new String[]{EnumData.SortEnum.TIME_DOWN.getStrName(), EnumData.SortEnum.TIME_UP.getStrName(), EnumData.SortEnum.SPEND_DOWN.getStrName(), EnumData.SortEnum.SPEND_UP.getStrName()})
                .positiveColor(getResources().getColor(R.color.negative_color))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int pos, CharSequence charSequence) {
                        sort = pos + 1;
                        MySp.setSort(sort);
                        refresh();
                        ctx.invalidateOptionsMenu();
                        materialDialog.cancel();
                    }
                })
                .callback(new MaterialDialog.FullCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }
                })
                .autoDismiss(false)
                .build();
        md.show();
    }

    void initOperate(final ProjectData pj) {

        final MaterialDialog md = new MaterialDialog.Builder(ctx)
                .title(XUtil.subStrDot(pj.getTitle(), 12))
                .positiveText("取消")
                .items(new String[]{"完成", "编辑", "删除"})
                .positiveColor(getResources().getColor(R.color.negative_color))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int pos, CharSequence charSequence) {

                        switch (pos) {
                            case 0:
                                pjFinish(pj);
                                break;
                            case 1:
                                Intent intent = new Intent(ctx, PjAddActivity.class);
                                intent.putExtra(Constant.KEY_PARAM_DATA, pj);
                                startActivity(intent);
                                break;
                            case 2:
                                delDlg(pj);
                                break;


                        }

                        materialDialog.cancel();
                    }
                })
                .callback(new MaterialDialog.FullCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }
                })
                .autoDismiss(false)
                .build();
        md.show();
    }

    void initProject() {
        pjList = new ArrayList<>();

        try {
            QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
            builder.where().eq("frequency", EnumData.FrequencyEnum.NORMAL.getValue()).and().eq("status", EnumData.StatusEnum.FINISH.getValue());
            builder.orderBy("id", false).limit(Constant.MAX_DB_QUERY);
            pjList = projectDao.query(builder.prepare());
        } catch (Exception e) {
        }
        if (!XUtil.listNotNull(pjList)) {
            XUtil.tShort("无已完成的日常~");
            return;
        }
        String item[] = new String[pjList.size()];
        for (int i = 0; i < pjList.size(); i++) {
            item[i] = pjList.get(i).getTitle();
        }

        final MaterialDialog md = new MaterialDialog.Builder(ctx)
                .positiveText("取消")
                .items(item)
                .positiveColor(getResources().getColor(R.color.negative_color))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int pos, CharSequence charSequence) {
                        ProjectData pj = pjList.get(pos);
                        if (pj != null) {
                            ProjectData tmp = new ProjectData(pj.getTitle(), pj.getDescribe(), pj.getExpect_time(), EnumData.FrequencyEnum.ONCE.getValue());
                            tmp.setEx1(String.valueOf(pj.getId()));
                            projectDao.create(tmp);
                            refresh();
                        }
                        materialDialog.cancel();
                    }
                })
                .callback(new MaterialDialog.FullCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }
                })
                .autoDismiss(false)
                .build();
        md.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (ctx.type == -1) {
//            menu.add("more").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            menu.add("time_line").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_timer_3)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.add("alarm_off").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_stop)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("add").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_add)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            SubMenu sub = menu.addSubMenu("");
            sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
            sub.add(0, 1, 0, "时光机");
            sub.add(0, 2, 0, "已完成");
            sub.add(0, 3, 0, "日常");
            sub.add(0, 6, 0, "日常统计");
            sub.add(0, 4, 0, EnumData.SortEnum.valueOf(sort).getStrName());
            sub.add(0, 7, 0, "功能介绍");
            sub.add(0, 8, 0, "意见反馈");
            sub.add(0, 5, 0, "退出");
            sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        }
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
        timeGo = DbUtils.timeOn();
        handler.postDelayed(runnable, 1000);
        getData(true);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (timeGo != null) {
                handler.postDelayed(this, 1000);
                String show = XUtil.hms(System.currentTimeMillis() - timeGo);
                if (ctx.type != EnumData.StatusEnum.FINISH.getValue()) {
                    ctx.mTextView.setText(show);
                } else {
                    ctx.mTextView.setText("");
                }
            } else {
                ctx.mTextView.setText("");
            }
        }
    };

    @Override
    public void onLoadMore() {
        getData(false);
    }


}
