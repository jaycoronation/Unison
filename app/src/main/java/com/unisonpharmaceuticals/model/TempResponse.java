package com.unisonpharmaceuticals.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kiran Patel on 08-Sep-18.
 */
public class TempResponse
{

    /**
     * staff_summary : {"name":"MANISH A. PARIKH","division":"Harmony","head_quarters":"SURATH7","month":"January","year":"2018"}
     * notseen : [{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":0,"doctor_id":"10379","doctor":"DR. HIMANI DOCTOR,MD,DGO","degree":"MD,DGO","last_visit":"","area":"Zampa bazar-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":1,"doctor_id":"10549","doctor":"DR. AMEE SHAH,MD (SKIN)","degree":"MD,SKIN","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":2,"doctor_id":"10556","doctor":"DR. ALOK KARULKAR,MS,DLO (ENT)","degree":"MS(ENT),DLO","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":3,"doctor_id":"10562","doctor":"DR. RAJIV SHAH,MS(ORTHO.)","degree":"MS,ORTHO","last_visit":"","area":"Surat-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":4,"doctor_id":"10576","doctor":"DR. SHASHANK P. PASSWALA,MD","degree":"MD","last_visit":"","area":"Surat-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":5,"doctor_id":"10580","doctor":"DR. PRAGNESH R. SHAH,MD,DNB,DM","degree":"MD,DNB,DM","last_visit":"","area":"Surat-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":6,"doctor_id":"10584","doctor":"DR. ASHISH JOSHI, MD","degree":"MD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":7,"doctor_id":"10585","doctor":"DR. R.C. SHAH, MD","degree":"MD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":8,"doctor_id":"10596","doctor":"DR. N.J. SHAH,MD,","degree":"MD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":9,"doctor_id":"10602","doctor":"DR. KETAN ZAVERI,MD","degree":"MD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":10,"doctor_id":"10625","doctor":"DR. ASHA BHATT,MD,DGO,","degree":"MD,DGO","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":11,"doctor_id":"10633","doctor":"DR. V.M. SHAH,MD,DGO,MRCOG(LONDON)","degree":"MD,DGO","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":12,"doctor_id":"10644","doctor":"DR. MRS. D.D. VAIDYA,MD,DGO,","degree":"MD,DGO","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":13,"doctor_id":"10645","doctor":"DR. R.C. GUPTA,MS(OB & GY)","degree":null,"last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":14,"doctor_id":"11169","doctor":"DR. SUNITA DOSHI,MD,DVD","degree":"MD,DVD","last_visit":"","area":"Surat-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":15,"doctor_id":"11170","doctor":"DR. NAINESH BHATT,MD,DVD","degree":"MD,DVD","last_visit":"","area":"Surat-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","ma     p_id":16,"doctor_id":"11752","doctor":"DR. VINOD H DAKORIA,MBBS","degree":"MBBS","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":17,"doctor_id":"11757","doctor":"DR. GIRISH H SHAH,DVD,MISTD","degree":"DVD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":18,"doctor_id":"11759","doctor":"DR. RANI SUDHIR SHAH,MD(SKIN)","degree":"MD,SKIN","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":19,"doctor_id":"11774","doctor":"DR. GIRISH Z SHAH,MD","degree":"MD","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":20,"doctor_id":"11784","doctor":"DR. BANKIM SHAH,BAMS","degree":"BAMS","last_visit":"","area":"Nanpura-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":21,"doctor_id":"11807","doctor":"DR. ARVIND K MALAVIA,MD","degree":"MD","last_visit":"","area":"Rander-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":22,"doctor_id":"11810","doctor":"DR. KALPAN PATEL,MD","degree":"MD","last_visit":"","area":"Rander-Surat"},{"category":"","reason":"Not Available","date_reason":"Yet to visit","other_reason":"","other_date_reason":"","map_id":23,"doctor_id":"11811","doctor":"DR. RAJESH TRIVEDI,MD","degree":"MD","last_visit":"","area":"Rander-Surat"}]
     * status : pending
     * doctor_summary : {"total_doctors":24,"gp_doctors":2,"cons_doctors":22,"imp_doctors":0,"pot_doctors":0}
     * reasons : ["Not Available","Town Not Visited","Out of Station","Refused","Clinic Close","Out of Country","New Call Report","Not Meet","Appointment Call","Transfer Area not cover","Deleted","Other"]
     * date_reasons : ["Yet to visit","Not Available","Out of Station","Refused","Clinic Close","Out of Country","New Call Report","Not Meet","Appointment Call","Transfer Area not cover","Deleted","Other"]
     * success : 1
     * message :
     */

    private StaffSummaryBean staff_summary;
    private String status;
    private DoctorSummaryBean doctor_summary;
    private int success;
    private String message;
    private List<NotseenBean> notseen;
    private List<String> reasons;
    private List<String> date_reasons;

    public StaffSummaryBean getStaff_summary() {
        return staff_summary;
    }

    public void setStaff_summary(StaffSummaryBean staff_summary) {
        this.staff_summary = staff_summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DoctorSummaryBean getDoctor_summary() {
        return doctor_summary;
    }

    public void setDoctor_summary(DoctorSummaryBean doctor_summary) {
        this.doctor_summary = doctor_summary;
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

    public List<NotseenBean> getNotseen() {
        return notseen;
    }

    public void setNotseen(List<NotseenBean> notseen) {
        this.notseen = notseen;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public List<String> getDate_reasons() {
        return date_reasons;
    }

    public void setDate_reasons(List<String> date_reasons) {
        this.date_reasons = date_reasons;
    }

    public static class StaffSummaryBean {
        /**
         * name : MANISH A. PARIKH
         * division : Harmony
         * head_quarters : SURATH7
         * month : January
         * year : 2018
         */

        private String name;
        private String division;
        private String head_quarters;
        private String month;
        private String year;

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

        public String getHead_quarters() {
            return head_quarters;
        }

        public void setHead_quarters(String head_quarters) {
            this.head_quarters = head_quarters;
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

    public static class DoctorSummaryBean {
        /**
         * total_doctors : 24
         * gp_doctors : 2
         * cons_doctors : 22
         * imp_doctors : 0
         * pot_doctors : 0
         */

        private int total_doctors;
        private int gp_doctors;
        private int cons_doctors;
        private int imp_doctors;
        private int pot_doctors;

        public int getTotal_doctors() {
            return total_doctors;
        }

        public void setTotal_doctors(int total_doctors) {
            this.total_doctors = total_doctors;
        }

        public int getGp_doctors() {
            return gp_doctors;
        }

        public void setGp_doctors(int gp_doctors) {
            this.gp_doctors = gp_doctors;
        }

        public int getCons_doctors() {
            return cons_doctors;
        }

        public void setCons_doctors(int cons_doctors) {
            this.cons_doctors = cons_doctors;
        }

        public int getImp_doctors() {
            return imp_doctors;
        }

        public void setImp_doctors(int imp_doctors) {
            this.imp_doctors = imp_doctors;
        }

        public int getPot_doctors() {
            return pot_doctors;
        }

        public void setPot_doctors(int pot_doctors) {
            this.pot_doctors = pot_doctors;
        }
    }

    public static class NotseenBean {
        /**
         * category :
         * reason : Not Available
         * date_reason : Yet to visit
         * other_reason :
         * other_date_reason :
         * map_id : 0
         * doctor_id : 10379
         * doctor : DR. HIMANI DOCTOR,MD,DGO
         * degree : MD,DGO
         * last_visit :
         * area : Zampa bazar-Surat
         * ma     p_id : 16
         */

        private String category;
        private String reason;
        private String date_reason;
        private String other_reason;
        private String other_date_reason;
        private int map_id;
        private String doctor_id;
        private String doctor;
        private String degree;
        private String last_visit;
        private String area;
        @SerializedName("ma     p_id")
        private int _$MaP_id299; // FIXME check this code

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getDate_reason() {
            return date_reason;
        }

        public void setDate_reason(String date_reason) {
            this.date_reason = date_reason;
        }

        public String getOther_reason() {
            return other_reason;
        }

        public void setOther_reason(String other_reason) {
            this.other_reason = other_reason;
        }

        public String getOther_date_reason() {
            return other_date_reason;
        }

        public void setOther_date_reason(String other_date_reason) {
            this.other_date_reason = other_date_reason;
        }

        public int getMap_id() {
            return map_id;
        }

        public void setMap_id(int map_id) {
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

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getLast_visit() {
            return last_visit;
        }

        public void setLast_visit(String last_visit) {
            this.last_visit = last_visit;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int get_$MaP_id299() {
            return _$MaP_id299;
        }

        public void set_$MaP_id299(int _$MaP_id299) {
            this._$MaP_id299 = _$MaP_id299;
        }
    }
}
