package com.zncm.mxtg.uitls;

import android.content.SharedPreferences;

import com.zncm.mxtg.data.EnumData;

public class MySp extends MySharedPreferences {
    private static final String STATE_PREFERENCE = "state_preference";
    private static SharedPreferences sharedPreferences;

    enum Key {
        app_version_code,
        default_tk,
        default_pj,
        last_tab,
        simple_model,
        theme,
        pwd,
        show_lunar,
        today_date,
        list_animation,
        sort
    }


    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getPreferences(STATE_PREFERENCE);
        return sharedPreferences;
    }
    public static void setSort(Integer sort) {
        putInt(getSharedPreferences(), Key.sort.toString(), sort);
    }

    public static Integer getSort() {
        return getInt(getSharedPreferences(), Key.sort.toString(), EnumData.SortEnum.TIME_DOWN.getValue());
    }

    public static void setLastTab(Integer last_tab) {
        putInt(getSharedPreferences(), Key.last_tab.toString(), last_tab);
    }

    public static Integer getLastTab() {
        return getInt(getSharedPreferences(), Key.last_tab.toString(), 0);
    }

    public static void setDefaultTk(Integer default_tk) {
        putInt(getSharedPreferences(), Key.default_tk.toString(), default_tk);
    }

    public static Integer getDefaultTk() {
        return getInt(getSharedPreferences(), Key.default_tk.toString(), 0);
    }

    public static void setDefaultPj(Integer default_pj) {
        putInt(getSharedPreferences(), Key.default_pj.toString(), default_pj);
    }

    public static Integer getDefaultPj() {
        return getInt(getSharedPreferences(), Key.default_pj.toString(), 0);
    }

    public static void setAppVersionCode(Integer app_version_code) {
        putInt(getSharedPreferences(), Key.app_version_code.toString(), app_version_code);
    }

    public static Integer getAppVersionCode() {
        return getInt(getSharedPreferences(), Key.app_version_code.toString(), 0);
    }

    public static void setTheme(Integer theme) {
        putInt(getSharedPreferences(), Key.theme.toString(), theme);
    }

    public static Integer getTheme() {
        return getInt(getSharedPreferences(), Key.theme.toString(), -12627531);
    }

    public static void setPwd(String pwd) {
        putString(getSharedPreferences(), Key.pwd.toString(), pwd);
    }

    public static String getPwd() {
        return getString(getSharedPreferences(), Key.pwd.toString(), "");
    }


    public static void setSimpleModel(Boolean simple_model) {
        putBoolean(getSharedPreferences(), Key.simple_model.toString(), simple_model);
    }

    public static Boolean getSimpleModel() {
        return getBoolean(getSharedPreferences(), Key.simple_model.toString(), false);
    }

    public static void setListAnimation(Boolean list_animation) {
        putBoolean(getSharedPreferences(), Key.list_animation.toString(), list_animation);
    }

    public static Boolean getListAnimation() {
        return getBoolean(getSharedPreferences(), Key.list_animation.toString(), true);
    }

    public static void setShowLunar(Boolean show_lunar) {
        putBoolean(getSharedPreferences(), Key.show_lunar.toString(), show_lunar);
    }

    public static Boolean getShowLunar() {
        return getBoolean(getSharedPreferences(), Key.show_lunar.toString(), true);
    }

    public static void setTodayDate(String today_date) {
        putString(getSharedPreferences(), Key.today_date.toString(), today_date);
    }

    public static String getTodayDate() {
        return getString(getSharedPreferences(), Key.today_date.toString(), "");
    }


}
