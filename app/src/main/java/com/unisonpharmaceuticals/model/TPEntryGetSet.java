package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 18-Jul-18.
 */
public class TPEntryGetSet
{
    String detail = "";
    String date = "";
    String tp_area = "";
    String tp_ww = "";
    String remark = "";
    boolean isLeave = false;
    boolean isHoliday = false;
    boolean isSunday = false;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTp_area() {
        return tp_area;
    }

    public void setTp_area(String tp_area) {
        this.tp_area = tp_area;
    }

    public String getTp_ww() {
        return tp_ww;
    }

    public void setTp_ww(String tp_ww) {
        this.tp_ww = tp_ww;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isLeave() {
        return isLeave;
    }

    public void setLeave(boolean leave) {
        isLeave = leave;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }
}
