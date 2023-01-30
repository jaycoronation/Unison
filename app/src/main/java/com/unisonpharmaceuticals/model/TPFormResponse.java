package com.unisonpharmaceuticals.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 24-Jul-18.
 */
public class TPFormResponse
{

    /**
     * days : [{"work_with":[],"area":"","day_type":0,"day_name":"Wed","date":1533061800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Thu","date":1533148200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Fri","date":1533234600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Sat","date":1533321000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1533407400,"area":"","work_with":[],"no_data":"true","remark":"","holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","day_type":0,"day_name":"Mon","date":1533493800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Tue","date":1533580200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Wed","date":1533666600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Thu","date":1533753000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Fri","date":1533839400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Sat","date":1533925800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1534012200,"area":"","work_with":[],"no_data":"true","remark":"","holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","day_type":0,"day_name":"Mon","date":1534098600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Tue","date":1534185000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Wed","date":1534271400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Thu","date":1534357800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Fri","date":1534444200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Sat","date":1534530600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1534617000,"area":"","work_with":[],"no_data":"true","remark":"","holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","day_type":0,"day_name":"Mon","date":1534703400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Tue","date":1534789800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Wed","date":1534876200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Thu","date":1534962600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Fri","date":1535049000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Sat","date":1535135400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1535221800,"area":"","work_with":[],"no_data":"true","remark":"","holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","day_type":0,"day_name":"Mon","date":1535308200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Tue","date":1535394600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Wed","date":1535481000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Thu","date":1535567400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","day_type":0,"day_name":"Fri","date":1535653800,"remark":"","no_data":"false","holiday":"false","leave":"false"}]
     * extradays : [{"area":"1-333","work_with":[{"staff_id":"6","staff":"Mayur Chatbar","code":"MYR"}],"day_type":1,"day_name":"Tue","date":"1530556200","remark":"test 2"},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""},{"area":"","work_with":[],"day_type":1,"day_name":"","date":"","remark":""}]
     * allData : {"reportDate":{"year":"2018","month":"August"}}
     * success : 1
     * message :
     */

    private AllDataBean allData;
    private int success = 0;
    private String message;
    private String status = "";
    private List<DaysBean> days;
    private List<ExtradaysBean> extradays;

    public AllDataBean getAllData() {
        return allData;
    }

    public void setAllData(AllDataBean allData) {
        this.allData = allData;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<ExtradaysBean> getExtradays() {
        return extradays;
    }

    public void setExtradays(List<ExtradaysBean> extradays) {
        this.extradays = extradays;
    }

    public static class AllDataBean {
        /**
         * reportDate : {"year":"2018","month":"August"}
         */

        private ReportDateBean reportDate;

        public ReportDateBean getReportDate() {
            return reportDate;
        }

        public void setReportDate(ReportDateBean reportDate) {
            this.reportDate = reportDate;
        }

        public static class ReportDateBean {
            /**
             * year : 2018
             * month : August
             */

            private String year;
            private String month;

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }
        }
    }

    public static class DaysBean {
        /**
         * work_with : []
         * area :
         * day_type : 0
         * day_name : Wed
         * date : 1533061800
         * remark :
         * no_data : false
         * holiday : false
         * leave : false
         */

        private String day_id = "";
        private String area = ""; // For '-' separated rout_area_id and area_id
        private String area_name = "";// for actual area name
        private int day_type = 0;
        private String day_name = "";
        private long date = 0;
        private String remark = "";
        private String no_data = "";
        private String holiday = "";
        private String leave = "";
        private String work_withString = "";//comma separated ww id
        private String work_withName = "";//comma separated ww name
        private String route_area_id = "";//for actual route area id
        private String date_no = "";
        private ArrayList<ExtradaysBean.WorkWithBean> work_with; // this list is only use for when form is load, at that time ww array is coming from api

        public String getWork_withName() {
            return work_withName;
        }

        public void setWork_withName(String work_withName) {
            this.work_withName = work_withName;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public int getDay_type() {
            return day_type;
        }

        public void setDay_type(int day_type) {
            this.day_type = day_type;
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

        public String getWork_withString() {
            return work_withString;
        }

        public void setWork_withString(String work_withString) {
            this.work_withString = work_withString;
        }

        public ArrayList<ExtradaysBean.WorkWithBean> getWork_with() {
            return work_with;
        }

        public void setWork_with(ArrayList<ExtradaysBean.WorkWithBean> work_with) {
            this.work_with = work_with;
        }

        public String getRoute_area_id() {
            return route_area_id;
        }

        public void setRoute_area_id(String route_area_id) {
            this.route_area_id = route_area_id;
        }

        public String getDate_no() {
            return date_no;
        }

        public void setDate_no(String date_no) {
            this.date_no = date_no;
        }

        public String getDay_id() {
            return day_id;
        }

        public void setDay_id(String day_id) {
            this.day_id = day_id;
        }
    }

    public static class ExtradaysBean {
        /**
         * area : 1-333
         * work_with : [{"staff_id":"6","staff":"Mayur Chatbar","code":"MYR"}]
         * day_type : 1
         * day_name : Tue
         * date : 1530556200
         * remark : test 2
         */

        private String area = "";
        private String area_id = "";
        private String area_name = "";
        private int day_type = 0;
        private String day_name = "";
        private String date = "";
        private String remark = "";
        private List<WorkWithBean> work_with;
        private String work_withString = "";
        private String work_withName = "";
        private String sr_no = "";
        private String route_area_id = "";
        private String day_id = "";

        public String getSr_no() {
            return sr_no;
        }

        public void setSr_no(String sr_no) {
            this.sr_no = sr_no;
        }

        public String getWork_withName() {
            return work_withName;
        }

        public void setWork_withName(String work_withName) {
            this.work_withName = work_withName;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getDay_type() {
            return day_type;
        }

        public void setDay_type(int day_type) {
            this.day_type = day_type;
        }

        public String getDay_name() {
            return day_name;
        }

        public void setDay_name(String day_name) {
            this.day_name = day_name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<WorkWithBean> getWork_with() {
            return work_with;
        }

        public void setWork_with(List<WorkWithBean> work_with) {
            this.work_with = work_with;
        }

        public String getWork_withString() {
            return work_withString;
        }

        public void setWork_withString(String work_withString) {
            this.work_withString = work_withString;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getRoute_area_id() {
            return route_area_id;
        }

        public void setRoute_area_id(String route_area_id) {
            this.route_area_id = route_area_id;
        }

        public String getDay_id() {
            return day_id;
        }

        public void setDay_id(String day_id) {
            this.day_id = day_id;
        }

        public static class WorkWithBean {
            /**
             * staff_id : 6
             * staff : Mayur Chatbar
             * code : MYR
             */

            private String staff_id;
            private String staff;
            private String code;

            public String getStaff_id() {
                return staff_id;
            }

            public void setStaff_id(String staff_id) {
                this.staff_id = staff_id;
            }

            public String getStaff() {
                return staff;
            }

            public void setStaff(String staff) {
                this.staff = staff;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}
