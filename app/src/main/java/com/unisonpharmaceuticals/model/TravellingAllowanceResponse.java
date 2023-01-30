package com.unisonpharmaceuticals.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 10-Aug-18.
 */
public class TravellingAllowanceResponse
{
    /**
     * staff : {"name":"MANISH A. PARIKH","division":"Harmony","hq":"SURATH7","month":"August","year":"2018"}
     * expences : {"mobile_expence":"403","internet_expence":"1","adjustment":"500"}
     * days : [{"day_id":"1","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":100,"chemist":50,"total":680,"day_type":"0","doctors":"0","business":"0","day_name":"Wed","date":1533061800,"remark":"test","no_data":"false","holiday":"false","leave":"false"},{"day_id":"2","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":100,"chemist":0,"total":468,"day_type":"0","doctors":"0","business":"0","day_name":"Thu","date":1533148200,"remark":"test 2","no_data":"false","holiday":"false","leave":"false"},{"day_id":"3","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":300,"chemist":0,"total":668,"day_type":"0","doctors":"0","business":"0","day_name":"Fri","date":1533234600,"remark":"test 3","no_data":"false","holiday":"false","leave":"false"},{"day_id":"4","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":400,"chemist":0,"total":768,"day_type":"0","doctors":"0","business":"0","day_name":"Sat","date":1533321000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"5","tp_name":"Sunday","src":"","distance":"","travelling_mode":"","HQ":"","ON":"","UD":"","fare":"","sundry":0,"chemist":0,"total":0,"day_type":3,"doctors":"0","business":"0","day_name":"Sun","date":1533407400,"remark":"","nodata":"true","holiday":"true","leave":"false"},{"day_id":"6","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Mon","date":1533493800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"7","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":80,"chemist":10,"total":620,"day_type":"0","doctors":"2","business":"1500","day_name":"Tue","date":1533580200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"8","tp_name":"A K Road","src":"A.k. road","distance":"0","travelling_mode":"Bike","HQ":"250","ON":0,"UD":0,"fare":"0","sundry":0,"chemist":0,"total":250,"day_type":"0","doctors":"0","business":"0","day_name":"Wed","date":1533666600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"9","tp_name":"A K Road","src":"A.k. road","distance":"0","travelling_mode":"Bike","HQ":"250","ON":0,"UD":0,"fare":"0","sundry":0,"chemist":0,"total":250,"day_type":"0","doctors":"0","business":"0","day_name":"Thu","date":1533753000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"10","tp_name":"A K Road","src":"A.k. road","distance":"0","travelling_mode":"Bike","HQ":"250","ON":0,"UD":0,"fare":"0","sundry":0,"chemist":0,"total":250,"day_type":"0","doctors":"6","business":"3800","day_name":"Fri","date":1533839400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"11","tp_name":"A K Road","src":"A.k. road","distance":"0","travelling_mode":"Bike","HQ":"250","ON":0,"UD":0,"fare":"0","sundry":0,"chemist":0,"total":250,"day_type":"0","doctors":"6","business":"3000","day_name":"Sat","date":1533925800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"12","tp_name":"Sunday","src":"","distance":"","travelling_mode":"","HQ":"","ON":"","UD":"","fare":"","sundry":0,"chemist":0,"total":0,"day_type":3,"doctors":"0","business":"0","day_name":"Sun","date":1534012200,"remark":"","nodata":"true","holiday":"true","leave":"false"},{"day_id":"13","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"     0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Mon","date":1534098600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"14","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Tue","date":1534185000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"15","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Wed","date":1534271400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"16","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Thu","date":1534357800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"17","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"1","business":"500","day_name":"Fri","date":1534444200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"18","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Sat","date":1534530600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"19","tp_name":"Sunday","src":"","distance":"","travelling_mode":"","HQ":"","ON":"","UD":"","fare":"","sundry":0,"chemist":0,"total":0,"day_type":3,"doctors":"0","business":"0","day_name":"Sun","date":1534617000,"remark":"","nodata":"true","holiday":"true","leave":"false"},{"day_id":"20","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Mon","date":1534703400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"21","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Tue","date":1534789800,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"22","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Wed","date":1534876200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"23","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Thu","date":1534962600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"24","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Fri","date":1535049000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"25","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Sat","date":1535135400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"26","tp_name":"Sunday","src":"","distance":"","travelling_mode":"","HQ":"","ON":"","UD":"","fare":"","sundry":0,"chemist":0,"total":0,"day_type":3,"doctors":"0","business":"0"," 08-20 04:56:26.344 1759-1787/com.unison D/OkHttp: day_name":"Sun","date":1535221800,"remark":"","nodata":"true","holiday":"true","leave":"false"},{"day_id":"27","tp_name":"Amroli","src":"Amroli","distance":"0","travelling_mode":"Bike","HQ":0,"ON":"530","UD":0,"fare":"0","sundry":0,"chemist":0,"total":530,"day_type":"0","doctors":"0","business":"0","day_name":"Mon","date":1535308200,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"28","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":0,"chemist":0,"total":368,"day_type":"0","doctors":"0","business":"0","day_name":"Tue","date":1535394600,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"29","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":0,"chemist":0,"total":368,"day_type":"0","doctors":"0","business":"0","day_name":"Wed","date":1535481000,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"30","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":0,"chemist":0,"total":368,"day_type":"0","doctors":"0","business":"0","day_name":"Thu","date":1535567400,"remark":"","no_data":"false","holiday":"false","leave":"false"},{"day_id":"31","tp_name":"DANDI BARBODHAN","src":"Dandi","distance":"45","travelling_mode":"Bike","HQ":0,"ON":0,"UD":"255","fare":"113","sundry":0,"chemist":0,"total":368,"day_type":"0","doctors":"0","business":"0","day_name":"Fri","date":1535653800,"remark":"","no_data":"false","holiday":"false","leave":"false"}]
     * status : confirmed
     * success : 1
     * message :
     */

    private StaffBean staff = new StaffBean();
    private ExpencesBean expences = new ExpencesBean();
    private String status = "";
    private int success = 0;
    private String message = "";
    private List<DaysBean> days = new ArrayList<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
         * name : MANISH A. PARIKH
         * division : Harmony
         * hq : SURATH7
         * month : August
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

    public static class ExpencesBean {
        /**
         * mobile_expence : 403
         * internet_expence : 1
         * adjustment : 500
         */

        private String mobile_expence = "";
        private String internet_expence = "";
        private String adjustment = "";

        public String getMobile_expence() {
            return mobile_expence;
        }

        public void setMobile_expence(String mobile_expence) {
            this.mobile_expence = mobile_expence;
        }

        public String getInternet_expence() {
            return internet_expence;
        }

        public void setInternet_expence(String internet_expence) {
            this.internet_expence = internet_expence;
        }

        public String getAdjustment() {
            return adjustment;
        }

        public void setAdjustment(String adjustment) {
            this.adjustment = adjustment;
        }
    }

    public static class DaysBean {
        /**
         * day_id : 1
         * tp_name : Amroli
         * src : Amroli
         * distance : 0
         * travelling_mode : Bike
         * HQ : 0
         * ON : 530
         * UD : 0
         * fare : 0
         * sundry : 100
         * chemist : 50
         * total : 680
         * day_type : 0
         * doctors : 0
         * business : 0
         * day_name : Wed
         * date : 1533061800
         * remark : test
         * no_data : false
         * holiday : false
         * leave : false
         * nodata : true
         *  08-20 04:56:26.344 1759-1787/com.unison D/OkHttp: day_name : Sun
         */

        private String day_id = "";
        private String tp_name = "";
        private String src = "";
        private String distance = "0";
        private String travelling_mode = "";
        private String HQ = "0";
        private String ON = "0";
        private String UD = "0";
        private String fare = "0";
        private int sundry = 0;
        private int chemist = 0;
        private int total = 0;
        private String day_type = "";
        private String doctors = "0";
        private String business = "0";
        private String day_name = "";
        private long date = 0;
        private String remark = "";
        private String no_data = "";
        private String holiday = "";
        private String leave = "";


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

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTravelling_mode() {
            return travelling_mode;
        }

        public void setTravelling_mode(String travelling_mode) {
            this.travelling_mode = travelling_mode;
        }

        public String getHQ() {
            return HQ;
        }

        public void setHQ(String HQ) {
            this.HQ = HQ;
        }

        public String getON() {
            return ON;
        }

        public void setON(String ON) {
            this.ON = ON;
        }

        public String getUD() {
            return UD;
        }

        public void setUD(String UD) {
            this.UD = UD;
        }

        public String getFare() {
            return fare;
        }

        public void setFare(String fare) {
            this.fare = fare;
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

        public String getDay_type() {
            return day_type;
        }

        public void setDay_type(String day_type) {
            this.day_type = day_type;
        }

        public String getDoctors() {
            return doctors;
        }

        public void setDoctors(String doctors) {
            this.doctors = doctors;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
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
    }




   /* private int success = 0;
    private String message = "";
    private List<DaysBean> days;

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

    public static class DaysBean {
        *//**
         * day_id : 1
         * tp_name : Amroli
         * src : Amroli
         * distance : 0
         * travelling_mode : Bike
         * HQ : 0
         * ON : 530
         * UD : 0
         * fare : 0
         * sundry : 0
         * chemist : 0
         * total : 530
         * day_type : 0
         * doctors : 0
         * business : 0
         * day_name : Wed
         * date : 1533061800
         * remark :
         * no_data : false
         * holiday : false
         * leave : false
         * nodata : true
         *//*

        private String day_id = "";
        private String tp_name = "";
        private String src = "";
        private String distance = "";
        private String travelling_mode = "";
        private String HQ = "";
        private String ON = "";
        private String UD = "";
        private String fare = "";
        private int sundry = 0;
        private int chemist = 0;
        private int total = 0;
        private String day_type = "";
        private String doctors = "";
        private String business = "";
        private String day_name = "";
        private long date = 0;
        private String remark = "";
        private String no_data = "";
        private String holiday = "";
        private String leave = "";

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

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTravelling_mode() {
            return travelling_mode;
        }

        public void setTravelling_mode(String travelling_mode) {
            this.travelling_mode = travelling_mode;
        }

        public String getHQ() {
            return HQ;
        }

        public void setHQ(String HQ) {
            this.HQ = HQ;
        }

        public String getON() {
            return ON;
        }

        public void setON(String ON) {
            this.ON = ON;
        }

        public String getUD() {
            return UD;
        }

        public void setUD(String UD) {
            this.UD = UD;
        }

        public String getFare() {
            return fare;
        }

        public void setFare(String fare) {
            this.fare = fare;
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

        public String getDay_type() {
            return day_type;
        }

        public void setDay_type(String day_type) {
            this.day_type = day_type;
        }

        public String getDoctors() {
            return doctors;
        }

        public void setDoctors(String doctors) {
            this.doctors = doctors;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
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
    }*/
}
