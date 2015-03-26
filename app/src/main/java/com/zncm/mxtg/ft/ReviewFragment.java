package com.zncm.mxtg.ft;

import android.content.Intent;
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
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.j256.ormlite.stmt.QueryBuilder;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxtg.R;
import com.zncm.mxtg.adapter.ReviewAdapter;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.ui.DbUtils;
import com.zncm.mxtg.ui.PjAddActivity;
import com.zncm.mxtg.ui.ReviewActivity;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.MySp;
import com.zncm.mxtg.uitls.XUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReviewFragment extends BaseListFragment implements DatePickerDialog.OnDateSetListener {
    private ReviewAdapter mAdapter;
    private ReviewActivity ctx;
    private List<ProjectData> datas = null;
    private boolean onLoading = false;
    private Long dayTime;
    List<ProjectData> pjList;
    private Calendar mCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (ReviewActivity) getActivity();


        datas = new ArrayList<ProjectData>();
        mAdapter = new ReviewAdapter(ctx) {
            @Override
            public void setData(int position, final ReviewAdapter.PjViewHolder holder) {
                final ProjectData pj = datas.get(position);
                if (pj == null) {
                    return;
                }


                if (XUtil.notEmptyOrNull(pj.getTitle())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(pj.getTitle());
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }

                Long stop = pj.getStop_time();
                Long start = pj.getStart_time();


                if (!ctx.bDay) {
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

                }


                String fromTo = "";

                if (start != null && start > 0) {
                    fromTo = XUtil.getDateHM(start);
                }
                if (stop != null && stop > 0) {
                    fromTo = fromTo + "-" + XUtil.getDateHM(stop);
                }
                if (XUtil.notEmptyOrNull(fromTo)) {
                    holder.tvStopTime.setVisibility(View.VISIBLE);
                    holder.tvStopTime.setText(fromTo);
                } else {
                    holder.tvStopTime.setVisibility(View.GONE);
                }


                holder.tvSpendTime.setVisibility(View.VISIBLE);


                Long expect = pj.getExpect_time();
                Long spend = pj.getSpend_time();
                String show;
                if (spend != null && spend > 0) {
                    if (expect != null && expect > 0) {
                        float progress = spend * 100 / expect;
                        String finish = "";
                        if (progress >= 100) {
                            finish = "√";
                        } else {
                            finish = String.valueOf((int) progress) + "%";
                        }
                        show = XUtil.hms(expect) + "  " + finish;
                        holder.tvSpendTime.setText(XUtil.hms(spend) + " -> " + show);
                    } else {
                        holder.tvSpendTime.setText(XUtil.hms(spend));
                    }

                } else {
                    holder.tvSpendTime.setText("");
                }


                if (pj.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.accent));
                } else {
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.white));
                }


                if (pj.getFrequency() == EnumData.FrequencyEnum.NORMAL.getValue()) {
                    holder.tvTag.setText("日常");
                    holder.tvTag.setVisibility(View.VISIBLE);
                } else {
                    holder.tvTag.setVisibility(View.GONE);
                }


                holder.operate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initOperate(pj);
                    }
                });


            }


        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int curPosition = position - listView.getHeaderViewsCount();
                if (curPosition >= 0 && XUtil.listNotNull(datas) && XUtil.listNotNull(datas)) {
                    if (curPosition >= datas.size()) {
                        return;
                    }
                    ProjectData projectData = datas.get(curPosition);
                    if (projectData == null) {
                        return;
                    }
                    initOperate(projectData);
                }

            }
        });

        if (ctx.bDay) {
            mCalendar = Calendar.getInstance();
            dayTime = XUtil.getDayStart();
            emptyText.setText("当日无记录。");
        } else {
            emptyText.setText("暂无日常记录。");
        }

        refresh();
        return view;
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
        if (ctx.bDay) {
            ctx.mTextView.setText(XUtil.getDateMD(dayTime));
        } else {
            if (ctx.pj != null && XUtil.notEmptyOrNull(ctx.pj.getTitle())) {
                ctx.mTextView.setText(ctx.pj.getTitle());
            }
        }
        GetData getData = new GetData();
        getData.execute(bFirst);
    }

    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {

            boolean canLoadMore = true;
            try {
                QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();

                if (ctx.bDay) {
                    builder.where().between("stop_time", dayTime, dayTime + Constant.DAY).and().eq("status", EnumData.StatusEnum.FINISH.getValue());
                } else {
                    if (ctx.pj != null && XUtil.notEmptyOrNull(ctx.pj.getId() + "")) {
                        builder.where().eq("ex1", String.valueOf(ctx.pj.getId())).or().eq("id", String.valueOf(ctx.pj.getId())).and().eq("status", EnumData.StatusEnum.FINISH.getValue());
                    } else {
                        builder.where().isNotNull("ex1").and().eq("status", EnumData.StatusEnum.FINISH.getValue());
                    }
                }

                builder.orderBy("stop_time", false);
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


    void initOperate(final ProjectData pj) {

        final MaterialDialog md = new MaterialDialog.Builder(ctx)
                .title(XUtil.subStrDot(pj.getTitle(), 12))
                .positiveText("取消")
                .items(new String[]{"重启", "编辑", "删除"})
                .positiveColor(getResources().getColor(R.color.negative_color))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int pos, CharSequence charSequence) {

                        switch (pos) {
                            case 0:
                                pj.setStatus(EnumData.StatusEnum.OFF.getValue());
                                pj.setModify_time(XUtil.getLongTime());
                                projectDao.update(pj);
                                refresh();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (ctx.bDay) {
            menu.add("before").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_navigate_before)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("next").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_navigate_next)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("event").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_event)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
            menu.add("menu").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("edit").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_edit)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("before")) {
            dayTime = dayTime - Constant.DAY;
            refresh();
        } else if (item.getTitle().equals("next")) {
            dayTime = dayTime + Constant.DAY;
            refresh();
        } else if (item.getTitle().equals("menu")) {
            initProject();
        } else if (item.getTitle().equals("event")) {
            initDatePicker();

        } else if (item.getTitle().equals("edit")) {
            if (ctx.pj != null) {
                Intent intent = new Intent(ctx, PjAddActivity.class);
                intent.putExtra(Constant.KEY_PARAM_DATA, ctx.pj);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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
                            ctx.pj = pj;
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


    private void initDatePicker() {
        //mCalendar
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2000, 2025);
        datePickerDialog.show(getFragmentManager(), "datepicker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mCalendar.set(year, month, day, 0, 0, 0);
        dayTime = mCalendar.getTime().getTime();
        getData(true);
    }

}
