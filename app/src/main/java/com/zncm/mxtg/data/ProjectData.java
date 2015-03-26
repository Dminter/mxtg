package com.zncm.mxtg.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxtg.uitls.XUtil;

import java.io.Serializable;

public class ProjectData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String describe;
    @DatabaseField
    private Long expect_time; //期望完成耗时，分钟
    @DatabaseField
    private Long spend_time;
    @DatabaseField
    private Long start_time;
    @DatabaseField
    private Long stop_time;
    @DatabaseField
    private int status;//状态 1-ON 2-OFF 3-FINISH 4-DEL
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private int frequency;//频次 1-一次 2-重复
    @DatabaseField
    private String ex1; //关联id 日常项目，父项目
    @DatabaseField
    private String ex2;
    @DatabaseField
    private String ex3;
    @DatabaseField
    private String ex4;
    @DatabaseField
    private String ex5;


    public ProjectData() {
    }


    public ProjectData(String title, String describe, Long expect_time, int frequency) {
        this.title = title;
        this.describe = describe;
        this.expect_time = expect_time;
        this.start_time = XUtil.getLongTime();
        this.status = EnumData.StatusEnum.OFF.getValue();
        this.modify_time = XUtil.getLongTime();
        this.frequency = frequency;
        this.time = XUtil.getLongTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getModify_time() {
        return modify_time;
    }

    public void setModify_time(Long modify_time) {
        this.modify_time = modify_time;
    }


    public Long getExpect_time() {
        return expect_time;
    }

    public void setExpect_time(Long expect_time) {
        this.expect_time = expect_time;
    }


    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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


    public Long getSpend_time() {
        return spend_time;
    }

    public void setSpend_time(Long spend_time) {
        this.spend_time = spend_time;
    }
}
