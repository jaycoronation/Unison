package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 15-Oct-18.
 */
public class TAResponse
{

    /**
     * staff : {"name":"JAYENDRA N. PANCHAL ","division":"Pharma","hq":"BarodaP1","month":"October","year":"2018"}
     * expences : {"mobile_expence":0,"internet_expence":0,"adjustment":0}
     * days : [{"day_id":"","work_with":[],"tp_name":"Bhadran - Alkapuri - Manjalpur","day_type":"0","work_miss_match":"1","day_options":[{"src_id":"5","src_name":"Borsad - Bhadran - car","travelling_mode":"car","fare":"600","distance":"50","hq":0,"ud":"255","on":0,"is_default":"1"},{"src_id":"4","src_name":"Borsad - Bhadran - bike","travelling_mode":"bike","fare":"250","distance":"50","hq":0,"ud":"255","on":0,"is_default":"0"},{"src_id":"1","src_name":"Alkapuri - bike","travelling_mode":"bike","fare":"0","distance":"0","hq":"250","ud":0,"on":0,"is_default":"1"}],"day_name":"Thu","date":1539196200,"remark":"","no_data":"false","holiday":"false","leave":"false","doctors":0,"business":0,"sundry":0,"chemist":0,"total":1105},{"day_id":"","work_with":[],"tp_name":"","day_type":"0","work_miss_match":"1","day_options":[],"day_name":"Fri","date":1539282600,"remark":"","no_data":"false","holiday":"false","leave":"false","doctors":0,"business":0,"sundry":0,"chemist":0,"total":0}]
     * success : 1
     * message :
     */

    private StaffBean staff = new StaffBean();
    private ExpencesBean expences = new ExpencesBean();
    private int success = 0;
    private String message = "";
    private String status = "";
    private String is_manager_approved = "";
    private List<DaysBean> days = new ArrayList<>();
    private List<String> reasons;

    public String getIs_manager_approved() {
        return is_manager_approved;
    }

    public void setIs_manager_approved(String is_manager_approved) {
        this.is_manager_approved = is_manager_approved;
    }

    public StaffBean getStaff() {
        return staff;
    }

    public void setStaff(StaffBean staff) {
        this.staff = staff;
    }

    public ExpencesBean getExpences() {
        return expences;
    }

    public void setExpences(ExpencesBean expences) {
        this.expences = expences;
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

    public List<DaysBean> getDays() {
        return days;
    }

    public void setDays(List<DaysBean> days) {
        this.days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public static class StaffBean {
        /**
         * name : JAYENDRA N. PANCHAL
         * division : Pharma
         * hq : BarodaP1
         * month : October
         * year : 2018
         */

        private String approved_by = ""; // For get name who approved TA
        private String name = ""; // For get name who confirmed TA
        private String division = "";
        private String hq = "";
        private String month = "";
        private String year = "";

        public String getApproved_by() {
            return approved_by;
        }

        public void setApproved_by(String approved_by) {
            this.approved_by = approved_by;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getHq() {
            return hq;
        }

        public void setHq(String hq) {
            this.hq = hq;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class ExpencesBean {
        /**
         * mobile_expence : 0
         * internet_expence : 0
         * adjustment : 0
         */

        private int mobile_expence = 0;
        private int internet_expence = 0;
        private int adjustment = 0;

        public int getMobile_expence() {
            return mobile_expence;
        }

        public void setMobile_expence(int mobile_expence) {
            this.mobile_expence = mobile_expence;
        }

        public int getInternet_expence() {
            return internet_expence;
        }

        public void setInternet_expence(int internet_expence) {
            this.internet_expence = internet_expence;
        }

        public int getAdjustment() {
            return adjustment;
        }

        public void setAdjustment(int adjustment) {
            this.adjustment = adjustment;
        }
    }

    public static class DaysBean {
        /**
         * day_id :
         * work_with : []
         * tp_name : Bhadran - Alkapuri - Manjalpur
         * day_type : 0
         * work_miss_match : 1
         * day_options : [{"src_id":"5","src_name":"Borsad - Bhadran - car","travelling_mode":"car","fare":"600","distance":"50","hq":0,"ud":"255","on":0,"is_default":"1"},{"src_id":"4","src_name":"Borsad - Bhadran - bike","travelling_mode":"bike","fare":"250","distance":"50","hq":0,"ud":"255","on":0,"is_default":"0"},{"src_id":"1","src_name":"Alkapuri - bike","travelling_mode":"bike","fare":"0","distance":"0","hq":"250","ud":0,"on":0,"is_default":"1"}]
         * day_name : Thu
         * date : 1539196200
         * remark :
         * no_data : false
         * holiday : false
         * leave : false
         * doctors : 0
         * business : 0
         * sundry : 0
         * chemist : 0
         * total : 1105
         */

        private String selected_src_id = "";
        private String day_id = "";
        private String tp_name = "";
        private String day_type = "";
        private String work_miss_match = "";
        private String day_name = "";
        private long date = 0;
        private String remark = "";
        private String no_data = "";
        private String holiday = "";
        private String leave = "";
        private int doctors = 0;
        private int business = 0;
        private int sundry = 0;
        private int chemist = 0;
        private int total = 0;
        private List<?> work_with;
        private List<DayOptionsBean> day_options;

        public String getSelected_src_id() {
            return selected_src_id;
        }

        public void setSelected_src_id(String selected_src_id) {
            this.selected_src_id = selected_src_id;
        }

        public String getDay_id() {
            return day_id;
        }

        public void setDay_id(String day_id) {
            this.day_id = day_id;
        }

        public String getTp_name() {
            return tp_name;
        }

        public void setTp_name(String tp_name) {
            this.tp_name = tp_name;
        }

        public String getDay_type() {
            return day_type;
        }

        public void setDay_type(String day_type) {
            this.day_type = day_type;
        }

        public String getWork_miss_match() {
            return work_miss_match;
        }

        public void setWork_miss_match(String work_miss_match) {
            this.work_miss_match = work_miss_match;
        }

        public String getDay_name() {
            return day_name;
        }

        public void setDay_name(String day_name) {
            this.day_name = day_name;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
        public String getNo_data() {
            return no_data;
        }

        public void setNo_data(String no_data) {
            this.no_data = no_data;
        }

        public String getHoliday() {
            return holiday;
        }

        public void setHoliday(String holiday) {
            this.holiday = holiday;
        }

        public String getLeave() {
            return leave;
        }

        public void setLeave(String leave) {
            this.leave = leave;
        }

        public int getDoctors() {
            return doctors;
        }

        public void setDoctors(int doctors) {
            this.doctors = doctors;
        }

        public int getBusiness() {
            return business;
        }

        public void setBusiness(int business) {
            this.business = business;
        }

        public int getSundry() {
            return sundry;
        }

        public void setSundry(int sundry) {
            this.sundry = sundry;
        }

        public int getChemist() {
            return chemist;
        }

        public void setChemist(int chemist) {
            this.chemist = chemist;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<?> getWork_with() {
            return work_with;
        }

        public void setWork_with(List<?> work_with) {
            this.work_with = work_with;
        }

        public List<DayOptionsBean> getDay_options() {
            return day_options;
        }

        public void setDay_options(List<DayOptionsBean> day_options) {
            this.day_options = day_options;
        }

        public static class DayOptionsBean {
            /**
             * src_id : 5
             * src_name : Borsad - Bhadran - car
             * travelling_mode : car
             * fare : 600
             * distance : 50
             * hq : 0
             * ud : 255
             * on : 0
             * is_default : 1
             */

            private String src_id = "";
            private String src_name = "";
            private String travelling_mode = "";
            private String fare = "0";
            private String distance = "0";
            private String hq = "0";
            private String ud = "0";
            private String on = "0";
            private String is_default = "0";

            public String getSrc_id() {
                return src_id;
            }

            public void setSrc_id(String src_id) {
                this.src_id = src_id;
            }

            public String getSrc_name() {
                return src_name;
            }

            public void setSrc_name(String src_name) {
                this.src_name = src_name;
            }

            public String getTravelling_mode() {
                return travelling_mode;
            }

            public void setTravelling_mode(String travelling_mode) {
                this.travelling_mode = travelling_mode;
            }

            public String getFare() {
                return fare;
            }

            public void setFare(String fare) {
                this.fare = fare;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getHq() {
                return hq;
            }

            public void setHq(String hq) {
                this.hq = hq;
            }

            public String getUd() {
                return ud;
            }

            public void setUd(String ud) {
                this.ud = ud;
            }

            public String getOn() {
                return on;
            }

            public void setOn(String on) {
                this.on = on;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }
        }
    }
}
