package com.zncm.mxtg.ui;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.mxtg.data.EnumData;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.data.TimeData;
import com.zncm.mxtg.uitls.db.DatabaseHelper;
import com.zncm.mxtg.uitls.Constant;
import com.zncm.mxtg.uitls.XUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MX on 11/19 0019.
 */
public class DbUtils {


    static RuntimeExceptionDao<TimeData, Integer> timeDao;
    static RuntimeExceptionDao<ProjectData, Integer> projectDao;
    static DatabaseHelper databaseHelper = null;

    static DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    static void init() {
        if (timeDao == null) {
            timeDao = getHelper().getTdDao();
        }
        if (projectDao == null) {
            projectDao = getHelper().getPjDao();
        }

    }


    public static void timeSum(ProjectData pjData) {
        Long time = System.currentTimeMillis();
        init();
        try {
            QueryBuilder<TimeData, Integer> builder = timeDao.queryBuilder();
            builder.orderBy("time", false).limit(1);
            List<TimeData> list = timeDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                TimeData data = list.get(0);
                if (data != null && data.getStop_time() == null) {
                    data.setStop_time(time);
                    timeDao.update(data);
                    Long spend = time - data.getStart_time();
                    ProjectData projectData = projectDao.queryForId(data.getPj_id());
                    if (projectData != null) {
                        Long tmp = projectData.getSpend_time();
                        if (tmp == null) {
                            tmp = 0L;
                        }
                        projectData.setSpend_time(tmp + spend);
                        projectData.setStatus(EnumData.StatusEnum.OFF.getValue());
                        projectDao.update(projectData);
                    }
                    pjGo(pjData);
                } else {
                    pjGo(pjData);
                }
            } else {
                pjGo(pjData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Long timeOn() {
        Long time = null;
        init();
        try {
            QueryBuilder<TimeData, Integer> builder = timeDao.queryBuilder();
            builder.orderBy("time", false).limit(1);
            List<TimeData> list = timeDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                TimeData data = list.get(0);
                if (data != null && data.getStop_time() == null) {
                    time = data.getStart_time();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public static void delTimeByPj(int pj_id) {
        Long time = null;
        init();
        try {
            DeleteBuilder<TimeData, Integer> builder = timeDao.deleteBuilder();
            builder.where().eq("pj_id", pj_id);
            builder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void pjGo(ProjectData pjData) {
        TimeData timeData = new TimeData(pjData.getId(), pjData.getTitle());
        pjData.setStatus(EnumData.StatusEnum.ON.getValue());
        projectDao.update(pjData);
        timeDao.create(timeData);
    }

    public static void stopAll(ProjectData pj) {
        Long time = System.currentTimeMillis();
        init();
        try {
            QueryBuilder<TimeData, Integer> builder = timeDao.queryBuilder();
            builder.orderBy("time", false).limit(1);
            List<TimeData> list = timeDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                TimeData data = list.get(0);
                if (data != null && data.getStop_time() == null) {
                    if (pj != null) {
                        if (pj.getId() != data.getPj_id()) {
                            return;
                        }
                    }
                    data.setStop_time(time);
                    timeDao.update(data);
                    Long spend = time - data.getStart_time();
                    ProjectData projectData = projectDao.queryForId(data.getPj_id());
                    if (projectData != null) {
                        Long tmp = projectData.getSpend_time();
                        if (tmp == null) {
                            tmp = 0L;
                        }
                        projectData.setSpend_time(tmp + spend);
                        if (pj != null) {
                            projectData.setStatus(pj.getStatus());
                        } else {
                            projectData.setStatus(EnumData.StatusEnum.OFF.getValue());
                        }
                        projectDao.update(projectData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<ProjectData> getProjects() {
        init();
        ArrayList<ProjectData> datas = new ArrayList<ProjectData>();
        try {
            QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
            builder.where().notIn("status", EnumData.StatusEnum.DEL.getValue(), EnumData.StatusEnum.FINISH.getValue());
            builder.orderBy("start_time", true).limit(Constant.MAX_DB_QUERY);
            List<ProjectData> list = projectDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    public static ProjectData getLastFrequencyPj() {
        ProjectData pj = null;
        init();
        try {
            QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
            builder.where().eq("status", EnumData.StatusEnum.FINISH.getValue()).and().eq("frequency", EnumData.FrequencyEnum.NORMAL.getValue());
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            List<ProjectData> list = projectDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                pj = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pj;
    }


    public static void pjUpdate(ProjectData data) {
        init();
        try {
            if (data != null) {
                projectDao.update(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void pjDel(ProjectData data) {
        init();
        try {
            if (data != null) {
                projectDao.delete(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pjCreate(ProjectData data) {
        init();
        try {
            if (data != null) {
                projectDao.create(data);

                if (data.getFrequency() == EnumData.FrequencyEnum.NORMAL.getValue()) {
                    List<ProjectData> datas = new ArrayList<ProjectData>();
                    QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
                    builder.orderBy("id", false).limit(1l);
                    datas = (ArrayList<ProjectData>) projectDao.query(builder.prepare());
                    if (XUtil.listNotNull(datas)) {
                        ProjectData tmp = datas.get(0);
                        if (tmp != null) {
                            tmp.setEx1(String.valueOf(tmp.getId()));
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
