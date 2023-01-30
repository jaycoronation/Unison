package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 21-Sep-18.
 */
public class ViewTourplanResponse
{

    /**
     * staff : {"name":"MILIND D. DESAI ","division":"Harmony","hq":"SuratH1","month":"September","year":"2018"}
     * days : [{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Sat","date":1535740200,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1535826600,"time":"02September2018","area":"","area_name":"","work_with":[],"no_data":"true","tp_name":"Sunday","city":"","h_o_e":"","remark":null,"holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Mon","date":1535913000,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Tue","date":1535999400,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Wed","date":1536085800,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"area":"346-483","area_name":"Dandi","tp_name":"DANDI BARBODHAN","city":"Surat","h_o_e":"E","work_with":[],"day_type":"0","day_name":"Thu","date":1536172200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"area":"348-38","area_name":"Ahmedabad","tp_name":"MEETING","city":"Surat","h_o_e":"E","work_with":[{"staff_id":"170","staff":"MALAY J. PATEL ","code":"MJP"},{"staff_id":"175","staff":"JAGDISH J. SAVALIYA ","code":"JJS"},{"staff_id":"167","staff":"NILESH C. MEHTA ","code":"NCM"}],"day_type":"0","day_name":"Fri","date":1536258600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Sat","date":1536345000,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1536431400,"time":"09September2018","area":"","area_name":"","work_with":[],"no_data":"true","tp_name":"Sunday","city":"","h_o_e":"","remark":null,"holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Mon","date":1536517800,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Tue","date":1536604200,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Wed","date":1536690600,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Thu","date":1536777000,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Fri","date":1536863400,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Sat","date":1536949800,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1537036200,"time":"16September2018","area":"","area_name":"","work_with":[],"no_data":"true","tp_name":"Sunday","city":"","h_o_e":"","remark":null,"holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Mon","date":1537122600,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Tue","date":1537209000,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Wed","date":1537295400,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Thu","date":1537381800,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Fri","date":1537468200,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Sat","date":1537554600,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1537641000,"time":"23September2018","area":"","area_name":"","work_with":[],"no_data":"true","tp_name":"Sunday","city":"","h_o_e":"","remark":null,"holiday":"true","leave":"false","day_type":3},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Mon","date":1537727400,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Tue","date":1537813800,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Wed","date":1537900200,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Thu","date":1537986600,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Fri","date":1538073000,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"work_with":[],"area":"","area_name":"","day_type":0,"day_name":"Sat","date":1538159400,"city":"","h_o_e":"","tp_name":"","remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_name":"Sun","date":1538245800,"time":"30September2018","area":"","area_name":"","work_with":[],"no_data":"true","tp_name":"Sunday","city":"","h_o_e":"","remark":null,"holiday":"true","leave":"false","day_type":3}]
     * allData : {"reportDate":{"year":"2018","month":"September"}}
     * success : 1
     * message :
     */

    private StaffBean staff = new StaffBean();
    private AllDataBean allData = new AllDataBean();
    private int success = 0;
    private String message = "";
    private List<DaysBean> days = new ArrayList<>();

    public StaffBean getStaff() {
        return staff;
    }

    public void setStaff(StaffBean staff) {
        this.staff = staff;
    }

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

    public static class StaffBean {
        /**
         * name : MILIND D. DESAI
         * division : Harmony
         * hq : SuratH1
         * month : September
         * year : 2018
         */

        private String name = "";
        private String division = "";
        private String hq = "";
        private String month = "";
        private String year = "";

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

    public static class AllDataBean {
        /**
         * reportDate : {"year":"2018","month":"September"}
         */

        private ReportDateBean reportDate = new ReportDateBean();

        public ReportDateBean getReportDate() {
            return reportDate;
        }

        public void setReportDate(ReportDateBean reportDate) {
            this.reportDate = reportDate;
        }

        public static class ReportDateBean {
            /**
             * year : 2018
             * month : September
             */

            private String year = "";
            private String month = "";

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
         * area_name :
         * day_type : 0
         * day_name : Sat
         * date : 1535740200
         * city :
         * h_o_e :
         * tp_name :
         * remark :
         * no_data : false
         * holiday : false
         * leave : false
         * time : 02September2018
         */

        private String area = "";
        private String area_name = "";
        private int day_type = 0;
        private String day_name = "";
        private int date = 0;
        private String city = "";
        private String h_o_e = "";
        private String tp_name = "";
        private String remark = "";
        private String no_data = "";
        private String holiday = "";
        private String leave = "";
        private String time = "";
        private String from_city = "";
        private String to_city = "";
        private List<WorkWithBean> work_with  = new ArrayList<>();

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

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getH_o_e() {
            return h_o_e;
        }

        public void setH_o_e(String h_o_e) {
            this.h_o_e = h_o_e;
        }

        public String getTp_name() {
            return tp_name;
        }

        public void setTp_name(String tp_name) {
            this.tp_name = tp_name;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<WorkWithBean> getWork_with() {
            return work_with;
        }

        public void setWork_with(List<WorkWithBean> work_with) {
            this.work_with = work_with;
        }

        public String getFrom_city() {
            return from_city;
        }

        public void setFrom_city(String from_city) {
            this.from_city = from_city;
        }

        public String getTo_city() {
            return to_city;
        }

        public void setTo_city(String to_city) {
            this.to_city = to_city;
        }
    }

    public class WorkWithBean
    {

        /**
         * staff_id : 170
         * staff : MALAY J. PATEL
         * code : MJP
         */

        private String staff_id = "";
        private String staff = "";
        private String code = "";

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
