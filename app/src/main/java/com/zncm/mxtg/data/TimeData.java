package com.zncm.mxtg.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxtg.uitls.XUtil;

import java.io.Serializable;

public class TimeData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int pj_id;
    @DatabaseField
    private String pj_title;
    @DatabaseField
    private Long start_time;
    @DatabaseField
    private Long stop_time;
    @DatabaseField
    private Long time;
    @DatabaseField
    private String ex1;
    @DatabaseField
    private String ex2;
    @DatabaseField
    private String ex3;
    @DatabaseField
    private String ex4;
    @DatabaseField
    private String ex5;


    public TimeData() {
    }


    public TimeData(int pj_id, String pj_title) {
        this.pj_id = pj_id;
        this.pj_title = pj_title;
        this.start_time = XUtil.getLongTime();
        this.time = XUtil.getLongTime();
    }

    public String getPj_title() {
        return pj_title;
    }

    public void setPj_title(String pj_title) {
        this.pj_title = pj_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPj_id() {
        return pj_id;
    }

    public void setPj_id(int pj_id) {
        this.pj_id = pj_id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getStop_time() {
        return stop_time;
    }

    public void setStop_time(Long stop_time) {
        this.stop_time = stop_time;
    }

    public String getEx1() {
        return ex1;
    }

    public void setEx1(String ex1) {
        this.ex1 = ex1;
    }

    public String getEx2() {
        return ex2;
    }

    public void setEx2(String ex2) {
        this.ex2 = ex2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }

    public String getEx4() {
        return ex4;
    }

    public void setEx4(String ex4) {
        this.ex4 = ex4;
    }

    public String getEx5() {
        return ex5;
    }

    public void setEx5(String ex5) {
        this.ex5 = ex5;
    }
}
