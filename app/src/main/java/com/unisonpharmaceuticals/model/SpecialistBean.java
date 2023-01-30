package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 27-Jun-18.
 */
public class SpecialistBean
{

    /**
     * speciality : [{"speciality":"Urologist","speciality_id":"32","speciality_code":"0"}]
     * success : 1
     * message :
     */

    private int success;
    private String message;
    private List<SpecialityBean> speciality;

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

    public List<SpecialityBean> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<SpecialityBean> speciality) {
        this.speciality = speciality;
    }

    public static class SpecialityBean {
        /**
         * speciality : Urologist
         * speciality_id : 32
         * speciality_code : 0
         */

        private String speciality = "";
        private String speciality_id = "";
        private String speciality_code = "";


        public SpecialityBean() {
        }

        public SpecialityBean(String speciality, String speciality_id, String speciality_code) {
            this.speciality = speciality;
            this.speciality_id = speciality_id;
            this.speciality_code = speciality_code;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getSpeciality_id() {
            return speciality_id;
        }

        public void setSpeciality_id(String speciality_id) {
            this.speciality_id = speciality_id;
        }

        public String getSpeciality_code() {
            return speciality_code;
        }

        public void setSpeciality_code(String speciality_code) {
            this.speciality_code = speciality_code;
        }

    }
}
