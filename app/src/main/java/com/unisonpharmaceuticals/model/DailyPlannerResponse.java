package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 15-Dec-18.
 */
public class DailyPlannerResponse
{

    /**
     * staff : {"date":"16/12/2018"}
     * plan : [{"map_id":"74","doctor_id":"10562","doctor":"DR. RAJIV SHAH,MS(ORTHO.)","added_date":"15/12/2018","speciality_id":"23","speciality":"ORTHOPAEDIC","mon_business":"2300","category":"I","area_id":"3","area":"Surat","last_visit":"","focus_for":"","is_confirmed":"No","is_approved":"No","approved_by":"","work_with":"MDD,YCK,PVP","work_array":[{"id":"166","name":"MDD"},{"id":"339","name":"YCK"},{"id":"393","name":"PVP"}],"item":"DI1 5"}]
     * success : 1
     * message :
     */

    private StaffBean staff = new StaffBean();
    private int success = 0;
    private String message = "";
    private List<PlanBean> plan = new ArrayList<>();

    public StaffBean getStaff() {
        return staff;
    }

    public void setStaff(StaffBean staff) {
        this.staff = staff;
    }

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

    public List<PlanBean> getPlan() {
        return plan;
    }

    public void setPlan(List<PlanBean> plan) {
        this.plan = plan;
    }

    public static class StaffBean {
        /**
         * date : 16/12/2018
         */

        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class PlanBean {
        /**
         * map_id : 74
         * doctor_id : 10562
         * doctor : DR. RAJIV SHAH,MS(ORTHO.)
         * added_date : 15/12/2018
         * speciality_id : 23
         * speciality : ORTHOPAEDIC
         * mon_business : 2300
         * category : I
         * area_id : 3
         * area : Surat
         * last_visit :
         * focus_for :
         * is_confirmed : No
         * is_approved : No
         * approved_by :
         * work_with : MDD,YCK,PVP
         * work_array : [{"id":"166","name":"MDD"},{"id":"339","name":"YCK"},{"id":"393","name":"PVP"}]
         * item : DI1 5
         */

        private String map_id = "";
        private String doctor_id = "";
        private String doctor = "";
        private int enable_focus = 0;
        private String added_date = "";
        private String speciality_id = "";
        private String speciality = "";
        private String mon_business = "";
        private String category = "";
        private String area_id = "";
        private String area = "";
        private String last_visit = "";
        private String focus_for = "";
        private String is_confirmed = "";
        private String is_approved = "";
        private String approved_by = "";
        private String work_with = "";
        private String item = "";
        private List<WorkArrayBean> work_array = new ArrayList<>();

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

        public String getAdded_date() {
            return added_date;
        }

        public void setAdded_date(String added_date) {
            this.added_date = added_date;
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

        public String getMon_business() {
            return mon_business;
        }

        public void setMon_business(String mon_business) {
            this.mon_business = mon_business;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public List<WorkArrayBean> getWork_array() {
            return work_array;
        }

        public void setWork_array(List<WorkArrayBean> work_array) {
            this.work_array = work_array;
        }

        public int getEnable_focus() {
            return enable_focus;
        }

        public void setEnable_focus(int enable_focus) {
            this.enable_focus = enable_focus;
        }

        public static class WorkArrayBean {
            /**
             * id : 166
             * name : MDD
             */

            private String id = "";
            private String name = "";

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
