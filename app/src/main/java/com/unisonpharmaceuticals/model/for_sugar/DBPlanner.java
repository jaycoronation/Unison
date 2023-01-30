package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Kiran Patel on 15-Dec-18.
 */
public class DBPlanner extends SugarRecord<DBPlanner>
{

    private String doctor_id = "";
    private String doctor = "";
    private String speciality_id = "";
    private String speciality = "";
    private String area_id = "";
    private String area = "";
    private String work_with = "";
    private int enable_focus = 0;

    public DBPlanner() {
    }

    public DBPlanner(String doctor_id, String doctor, String speciality_id, String speciality, String area_id, String area, String work_with, int enable_focus) {
        this.doctor_id = doctor_id;
        this.doctor = doctor;
        this.speciality_id = speciality_id;
        this.speciality = speciality;
        this.area_id = area_id;
        this.area = area;
        this.work_with = work_with;
        this.enable_focus = enable_focus;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(String speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWork_with() {
        return work_with;
    }

    public void setWork_with(String work_with) {
        this.work_with = work_with;
    }

    public int getEnable_focus() {
        return enable_focus;
    }

    public void setEnable_focus(int enable_focus) {
        this.enable_focus = enable_focus;
    }
}
