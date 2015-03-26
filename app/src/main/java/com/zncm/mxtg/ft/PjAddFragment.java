package com.zncm.mxtg.ft;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxtg.R;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.data.TimeData;
import com.zncm.mxtg.ui.DbUtils;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.RefreshEvent;
import com.zncm.mxtg.uitls.XUtil;

import java.io.Serializable;

import de.greenrobot.event.EventBus;


public class PjAddFragment extends Fragment implements View.OnClickListener {
    private Activity ctx;
    protected View view;
    private EditText etTitle, etDescribe;
    private RelativeLayout rlCreateTime, rlExpectTime, rlFrequency;
    private TextView tvCreateTime, tvExpectTime, tvFrequency;
    Long expect_time;
    String expect[] = new String[]{"5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟", "40分钟", "50分钟", "1小时", "1.5小时", "2小时", "4小时", "8小时"};
    int frequency =EnumData.FrequencyEnum.ONCE.getValue();
    private ProjectData projectData;
    private boolean bUpadte = false;

    public PjAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        view = inflater.inflate(R.layout.activity_tk_add, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        projectData = (ProjectData) dataParam;
        ((ActionBarActivity) ctx).getSupportActionBar().setTitle("新增目标");
        if (projectData != null) {
            ((ActionBarActivity) ctx).getSupportActionBar().setTitle("编辑目标");
            bUpadte = true;
            etTitle.setFocusable(false);
            etDescribe.setFocusable(false);
            etTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(1);
                }
            });
            etDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(2);
                }
            });

            if (XUtil.notEmptyOrNull(projectData.getTitle())) {
                etTitle.setVisibility(View.VISIBLE);
                etTitle.setText(projectData.getTitle());
            } else {
                etTitle.setVisibility(View.INVISIBLE);
            }

            if (XUtil.notEmptyOrNull(projectData.getDescribe())) {
                etDescribe.setVisibility(View.VISIBLE);
                etDescribe.setText(projectData.getDescribe());
            } else {
                etDescribe.setVisibility(View.VISIBLE);
            }

            if (projectData.getTime() != null) {
                tvCreateTime.setVisibility(View.VISIBLE);
                tvCreateTime.setText(XUtil.getTimeYMDHM(projectData.getTime()));
            } else {
                tvCreateTime.setVisibility(View.INVISIBLE);
            }
            rlCreateTime.setVisibility(View.VISIBLE);


            Long expectTime = projectData.getExpect_time();
            if (expectTime != null && expectTime > 0) {
                tvExpectTime.setVisibility(View.VISIBLE);
                tvExpectTime.setText((int) (expectTime / Constant.MIN) + "分钟");
            }else {
                tvExpectTime.setVisibility(View.INVISIBLE);
            }
            int frequency = projectData.getFrequency();
            tvFrequency.setVisibility(View.VISIBLE);
            if (frequency==EnumData.FrequencyEnum.NORMAL.getValue()){
                tvFrequency.setText(EnumData.FrequencyEnum.NORMAL.getStrName());
            }else {
                tvFrequency.setText(EnumData.FrequencyEnum.ONCE.getStrName());
            }

        } else {

            tvExpectTime.setText("无期限");
            tvFrequency.setText("一次");
            rlCreateTime.setVisibility(View.GONE);
        }
        XUtil.autoKeyBoardShow(etTitle);
    }

    void initEtDlg(final int etId) {

        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setLines(5);
        editText.setHint("输入...");
        editText.setTextColor(getResources().getColor(R.color.black));
        if (etId == 1) {
            editText.setText(projectData.getTitle());
            editText.setSelection(projectData.getTitle().length());
        } else if (etId == 2) {
            editText.setText(projectData.getDescribe());
            editText.setSelection(projectData.getDescribe().length());
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);

        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view)
                .positiveText("修改")
                .positiveColor(getResources().getColor(R.color.positive_color))
                .negativeText("取消")
                .negativeColor(getResources().getColor(R.color.negative_color))
                .callback(new MaterialDialog.FullCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            if (etId == 1) {
                                projectData.setTitle(content);
                                etTitle.setText(content);
                            } else if (etId == 2) {
                                projectData.setDescribe(content);
                                etDescribe.setText(content);
                            }

                        } catch (Exception e) {
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        materialDialog.cancel();
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }


    private void initView() {
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescribe = (EditText) view.findViewById(R.id.etDescribe);
        rlCreateTime = (RelativeLayout) view.findViewById(R.id.rlCreateTime);
        rlExpectTime = (RelativeLayout) view.findViewById(R.id.rlExpectTime);
        rlFrequency = (RelativeLayout) view.findViewById(R.id.rlFrequency);
        tvExpectTime = (TextView) view.findViewById(R.id.tvExpectTime);
        tvFrequency = (TextView) view.findViewById(R.id.tvFrequency);
        tvCreateTime = (TextView) view.findViewById(R.id.tvCreateTime);
        rlExpectTime.setOnClickListener(this);
        rlFrequency.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!bUpadte) {
            menu.add("save").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_save)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
//            menu.add("update").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_edit)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("del").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_delete)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                updateBack();
                break;
        }

//        updateBack()

        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("save")) {
            String title = etTitle.getText().toString().toString();
            if (!XUtil.notEmptyOrNull(title)) {
                XUtil.tShort("填入标题!");
                return false;
            }
            String describe = etDescribe.getText().toString().toString();
            ProjectData projectData = new ProjectData(title, describe, expect_time, frequency);
            DbUtils.pjCreate(projectData);
            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));
            ctx.finish();
        }else if (item.getTitle().equals("del")) {
            delDlg();
        }
        return false;
    }




    public void updateBack() {
        String title = etTitle.getText().toString().toString();
        if (!XUtil.notEmptyOrNull(title)) {
            XUtil.tShort("填入标题!");
        }
        if (bUpadte) {
            projectData.setModify_time(XUtil.getLongTime());
            DbUtils.pjUpdate(projectData);
            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));
            ctx.finish();
        }
    }

    private void delDlg() {
        new MaterialDialog.Builder(ctx)
                .title("删除")
                .content("确认删除?")
                .theme(Theme.LIGHT)
                .positiveText("删除")
                .negativeText("取消")
                .callback(new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        DbUtils.pjDel(projectData);
                        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));
                        ctx.finish();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlFrequency:
                initFrequency();
                break;
            case R.id.rlExpectTime:
                initExpect();
                break;
        }

    }


    private void initExpect() {


        new MaterialDialog.Builder(ctx)
                .title("期望耗时")
                .items(expect)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvExpectTime.setVisibility(View.VISIBLE);
                        tvExpectTime.setText(expect[which]);
                        //  String expect[] = new String[]{"5分钟","10分钟","15分钟","20分钟","25分钟","30分钟","40分钟","50分钟","1小时","1.5小时","2小时","4小时","8小时"};
                        Long tmp = 0L;
                        switch (which) {
                            case 0:
                                tmp = 5 * Constant.MIN;
                                break;
                            case 1:
                                tmp = 10 * Constant.MIN;
                                break;
                            case 2:
                                tmp = 15 * Constant.MIN;
                                break;
                            case 3:
                                tmp = 20 * Constant.MIN;
                                break;
                            case 4:
                                tmp = 25 * Constant.MIN;
                                break;
                            case 5:
                                tmp = 30 * Constant.MIN;
                                break;
                            case 6:
                                tmp = 40 * Constant.MIN;
                                break;
                            case 7:
                                tmp = 50 * Constant.MIN;
                                break;
                            case 8:
                                tmp = 60 * Constant.MIN;
                                break;
                            case 9:
                                tmp = 90 * Constant.MIN;
                                break;
                            case 10:
                                tmp = 2 * Constant.HOUR;
                                break;
                            case 11:
                                tmp = 4 * Constant.HOUR;
                                break;
                            case 12:
                                tmp = 8 * Constant.HOUR;
                                break;
                        }
                        expect_time = tmp;
                        if (bUpadte) {
                            projectData.setExpect_time(expect_time);
                        }
                    }
                })
                .show();
    }


    private void initFrequency() {
        new MaterialDialog.Builder(ctx)
                .title("频次")
                .items(new String[]{EnumData.FrequencyEnum.ONCE.getStrName(), EnumData.FrequencyEnum.NORMAL.getStrName()})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        frequency = (which + 1);
                        tvFrequency.setVisibility(View.VISIBLE);
                        tvFrequency.setText(EnumData.FrequencyEnum.valueOf(which + 1).getStrName());
                        if (bUpadte) {
                            projectData.setFrequency(frequency);
                        }
                    }
                })
                .show();
    }

}
