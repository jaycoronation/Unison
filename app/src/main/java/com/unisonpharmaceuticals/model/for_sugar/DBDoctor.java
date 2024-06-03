package com.unisonpharmaceuticals.model.for_sugar;


import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

public class DBDoctor extends SugarRecord<DBDoctor> {
    /**
     * doctor : Semil shah
     * doctor_id : 11
     * speciality_code : 0
     */

    private String doctor;
    private String doctor_id;
    private String speciality_code;
    private String speciality_id;
    private String speciality = "";
    private String area_id = "";
    private int enable_focus = 0;

    public DBDoctor() {
    }



    public DBDoctor(String doctor, String doctor_id, String speciality_code,String area_id,String speciality_id,String speciality,int enable_focus) {
        this.doctor = doctor;
        this.doctor_id = doctor_id;
        this.speciality_code = speciality_code;
        this.area_id = area_id;
        this.speciality_id = speciality_id;
        this.speciality = speciality;
        this.enable_focus = enable_focus;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getSpeciality_code() {
        return speciality_code;
    }

    public void setSpeciality_code(String speciality_code) {
        this.speciality_code = speciality_code;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
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

    public int getEnable_focus() {
        return enable_focus;
    }

    public void setEnable_focus(int enable_focus) {
        this.enable_focus = enable_focus;
    }
}