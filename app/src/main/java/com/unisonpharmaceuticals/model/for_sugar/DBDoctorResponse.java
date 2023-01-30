package com.unisonpharmaceuticals.model.for_sugar;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tushar Vataliya on 13-Jul-18.
 */
public class DBDoctorResponse
{

    /**
     * total : 15
     * success : 1
     * message :
     * doctors : [{"doctor_master_id":"23","area_id":"688","speciality_id":"31","speciality":"SURGEON","doctor_name":"New Doctor 2","area_name":"Songadh","district_name":"Tapi","state_name":"Gujarat","city_name":"Tapi","monthly_business":null,"timestamp":"10 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"41","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"22","area_id":"85","speciality_id":"33","speciality":"NON","doctor_name":"Mr pawan kumar","area_name":"Anjar","district_name":"Kutch","state_name":"Gujarat","city_name":"Anjar","monthly_business":"1500","timestamp":"10 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"35","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"21","area_id":"42","speciality_id":"16","speciality":"MEDICAL OFFICERS","doctor_name":"NCR Doctor","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":null,"timestamp":"06 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"34","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"20","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor7","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"36","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"27","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"19","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor6","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"37","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"28","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"18","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor5","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"38","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"29","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"17","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor4","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"39","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"30","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"16","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor3","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"31","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"15","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor2","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"40","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"32","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"14","area_id":"42","speciality_id":"4","speciality":"CARDIOLOGIST","doctor_name":"Doctor1","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"05 July, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"33","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"13","area_id":"101","speciality_id":null,"speciality":null,"doctor_name":"chhatbar mayur","area_name":"Paldi","district_name":"Ahmedabad","state_name":"Gujarat","city_name":"Ahmedabad","monthly_business":null,"timestamp":"27 June, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"26","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"12","area_id":"101","speciality_id":null,"speciality":null,"doctor_name":"chhatbar mayur","area_name":"Paldi","district_name":"Ahmedabad","state_name":"Gujarat","city_name":"Ahmedabad","monthly_business":null,"timestamp":"27 June, 2018","divisions":[{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"25","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"11","area_id":"870","speciality_id":"32","speciality":"UROLOGIST","doctor_name":"Semil Shah","area_name":"V. s. hospital","district_name":"Ahmedabad","state_name":"Gujarat","city_name":"Ahmedabad","monthly_business":null,"timestamp":"26 June, 2018","divisions":[{"name":"Sanket Thakker <span class='font_12 light'>(MSR)<\/span>","name_preview":"Sanket Thakker","user_id":"9","map_id":"21","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"22","division":"Sonus","division_id":"7"},{"name":"Ravi Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Ravi Patel","user_id":"7","map_id":"23","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"20","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"5","area_id":"167","speciality_id":"3","speciality":"BON SET","doctor_name":"Dr. a.m. thukka","area_name":"Siddhpur","district_name":"Patan","state_name":"Gujarat","city_name":"Siddhpur","monthly_business":"1500","timestamp":"20 June, 2018","divisions":[{"name":"Sanket Thakker <span class='font_12 light'>(MSR)<\/span>","name_preview":"Sanket Thakker","user_id":"9","map_id":"15","division":"Pharma","division_id":"6"},{"name":"Kiran Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Kiran Patel","user_id":"8","map_id":"24","division":"Sonus","division_id":"7"},{"name":"Ravi Patel <span class='font_12 light'>(MSR)<\/span>","name_preview":"Ravi Patel","user_id":"7","map_id":"18","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"19","division":"Rhythm","division_id":"9"}]},{"doctor_master_id":"3","area_id":"42","speciality_id":"3","speciality":"BON SET","doctor_name":"Dr. a.a. dodiya","area_name":"Talod","district_name":"Sabarkantha","state_name":"Gujarat","city_name":"Talod","monthly_business":"1500","timestamp":"20 June, 2018","divisions":[{"name":"Sanket Thakker <span class='font_12 light'>(MSR)<\/span>","name_preview":"Sanket Thakker","user_id":"9","map_id":"16","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"17","division":"Rhythm","division_id":"9"}]}]
     */

    private String total = "";
    private int success = 0;
    private String message = "";
    private List<DoctorsBean> doctors = new ArrayList<>();

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public List<DoctorsBean> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorsBean> doctors) {
        this.doctors = doctors;
    }

    public static class DoctorsBean {
        /**
         * doctor_master_id : 23
         * area_id : 688
         * speciality_id : 31
         * speciality : SURGEON
         * doctor_name : New Doctor 2
         * area_name : Songadh
         * district_name : Tapi
         * state_name : Gujarat
         * city_name : Tapi
         * monthly_business : null
         * timestamp : 10 July, 2018
         * divisions : [{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Pharma","division_id":"6"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Sonus","division_id":"7"},{"name":"NA","name_preview":"","user_id":"","map_id":"","division":"Harmoney","division_id":"8"},{"name":"Mayur Chatbar <span class='font_12 light'>(MSR)<\/span>","name_preview":"Mayur Chatbar","user_id":"6","map_id":"41","division":"Rhythm","division_id":"9"}]
         */

        private String doctor_master_id = "";
        private String area_id = "";
        private String speciality_id = "";
        private int enable_focus = 0;
        private String speciality = "";
        private String doctor_name = "";
        private String area_name = "";
        private String district_name = "";
        private String state_name = "";
        private String city_name = "";
        private Object monthly_business;
        private String timestamp = "";
        private List<DivisionsBean> divisions;

        public String getDoctor_master_id() {
            return doctor_master_id;
        }

        public void setDoctor_master_id(String doctor_master_id) {
            this.doctor_master_id = doctor_master_id;
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

        public String getDoctor_name() {
            return doctor_name;
        }

        public void setDoctor_name(String doctor_name) {
            this.doctor_name = doctor_name;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public Object getMonthly_business() {
            return monthly_business;
        }

        public void setMonthly_business(Object monthly_business) {
            this.monthly_business = monthly_business;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public List<DivisionsBean> getDivisions() {
            return divisions;
        }

        public void setDivisions(List<DivisionsBean> divisions) {
            this.divisions = divisions;
        }

        public int getEnable_focus() {
            return enable_focus;
        }

        public void setEnable_focus(int enable_focus) {
            this.enable_focus = enable_focus;
        }

        public static class DivisionsBean {
            /**
             * name : NA
             * name_preview :
             * user_id :
             * map_id :
             * division : Pharma
             * division_id : 6
             */

            private String name;
            private String name_preview;
            private String user_id;
            private String map_id;
            private String division;
            private String division_id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName_preview() {
                return name_preview;
            }

            public void setName_preview(String name_preview) {
                this.name_preview = name_preview;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getMap_id() {
                return map_id;
            }

            public void setMap_id(String map_id) {
                this.map_id = map_id;
            }

            public String getDivision() {
                return division;
            }

            public void setDivision(String division) {
                this.division = division;
            }

            public String getDivision_id() {
                return division_id;
            }

            public void setDivision_id(String division_id) {
                this.division_id = division_id;
            }
        }
    }
}
