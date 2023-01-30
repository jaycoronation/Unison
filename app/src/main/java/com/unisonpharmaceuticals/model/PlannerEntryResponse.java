package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Kiran Patel on 06-Aug-18.
 */
public class PlannerEntryResponse
{

    /**
     * plan : [{"map_id":"5","doctor_id":"3","doctor":"Dr. a.a. dodiya","mon_business":0,"category":"","area":"Talod","last_visit":"","focus_for":"","is_confirmed":"Yes","is_approved":"Yes","approved_by":"Kiran Patel"},{"map_id":"7","doctor_id":"5","doctor":"Dr. a.m. thukka","mon_business":0,"category":"","area":"Siddhpur","last_visit":"","focus_for":"","is_confirmed":"Yes","is_approved":"Yes","approved_by":"Kiran Patel"}]
     * success : 1
     * message :
     * plan_status : 2
     */

    private int success = 0;
    private String message = "";
    private String plan_status = "";
    private List<PlanBean> plan;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlan_status() {
        return plan_status;
    }

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public List<PlanBean> getPlan() {
        return plan;
    }

    public void setPlan(List<PlanBean> plan) {
        this.plan = plan;
    }

    public static class PlanBean {
        /**
         * map_id : 5
         * doctor_id : 3
         * doctor : Dr. a.a. dodiya
         * mon_business : 0
         * category :
         * area : Talod
         * last_visit :
         * focus_for :
         * is_confirmed : Yes
         * is_approved : Yes
         * approved_by : Kiran Patel
         */

        private String map_id = "";
        private String doctor_id = "";
        private String doctor = "";
        private float mon_business = 0;
        private String category = "";
        private String focus_new = "";
        private String focus_enhence="";
        private String speciality = "";
        private String area = "";
        private String last_visit = "";
        private String focus_for = "";
        private String is_confirmed = "";
        private String is_approved = "";
        private String approved_by = "";
        private String added_date = "";
        private String work_with = "";
        private String item = "";

        public String getMap_id() {
            return map_id;
        }

        public void setMap_id(String map_id) {
            this.map_id = map_id;
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

        public float getMon_business() {
            return mon_business;
        }

        public void setMon_business(float mon_business) {
            this.mon_business = mon_business;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLast_visit() {
            return last_visit;
        }

        public void setLast_visit(String last_visit) {
            this.last_visit = last_visit;
        }

        public String getFocus_for() {
            return focus_for;
        }

        public void setFocus_for(String focus_for) {
            this.focus_for = focus_for;
        }

        public String getIs_confirmed() {
            return is_confirmed;
        }

        public void setIs_confirmed(String is_confirmed) {
            this.is_confirmed = is_confirmed;
        }

        public String getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(String is_approved) {
            this.is_approved = is_approved;
        }

        public String getApproved_by() {
            return approved_by;
        }

        public void setApproved_by(String approved_by) {
            this.approved_by = approved_by;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
        }

        public String getWork_with() {
            return work_with;
        }

        public void setWork_with(String work_with) {
            this.work_with = work_with;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getFocus_new() {
            return focus_new;
        }

        public void setFocus_new(String focus_new) {
            this.focus_new = focus_new;
        }

        public String getFocus_enhence() {
            return focus_enhence;
        }

        public void setFocus_enhence(String focus_enhence) {
            this.focus_enhence = focus_enhence;
        }
    }
}
