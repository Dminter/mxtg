package com.zncm.mxtg.uitls.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zncm.mxtg.data.ProjectData;
import com.zncm.mxtg.data.TimeData;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mxtg.db";
    private static final int DATABASE_VERSION = 11;
    private RuntimeExceptionDao<ProjectData, Integer> pjDao = null;
    private RuntimeExceptionDao<TimeData, Integer> tdDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, ProjectData.class);
            TableUtils.createTableIfNotExists(connectionSource, TimeData.class);

            updateColumn(db, "timedata", "pj_title", "varchar");

        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }


    public synchronized void updateColumn(SQLiteDatabase db, String tableName,
                                          String columnName, String columnType) {
        try {
            if (db != null) {
                Cursor c = db.rawQuery("SELECT * from " + tableName
                        + " limit 1 ", null);
                boolean flag = false;

                if (c != null) {
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        if (columnName.equalsIgnoreCase(c.getColumnName(i))) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        String sql = "alter table " + tableName + " add "
                                + columnName + " " + columnType;
                        db.execSQL(sql);
                    }
                    c.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            onCreate(db, connectionSource);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public RuntimeExceptionDao<ProjectData, Integer> getPjDao() {
        if (pjDao == null) {
            pjDao = getRuntimeExceptionDao(ProjectData.class);
        }
        return pjDao;
    }

    public RuntimeExceptionDao<TimeData, Integer> getTdDao() {
        if (tdDao == null) {
            tdDao = getRuntimeExceptionDao(TimeData.class);
        }
        return tdDao;
    }


    @Override
    public void close() {
        super.close();
        pjDao = null;
        tdDao = null;
    }
}
