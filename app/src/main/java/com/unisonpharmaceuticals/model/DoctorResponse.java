package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 28-Jun-18.
 */
public class DoctorResponse
{

    /**
     * doctors : [{"doctor":"Semil shah","doctor_id":"11","speciality_code":"0"}]
     * success : 1
     * message :
     */

    private int success;
    private String message;
    private List<DoctorsBean> doctors;

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
         * doctor : Semil shah
         * doctor_id : 11
         * speciality_code : 0
         */

        private String doctor;
        private String doctor_id;
        private String speciality_code;
        private boolean selected = false;
        private int enable_focus = 0;


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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getEnable_focus() {
            return enable_focus;
        }

        public void setEnable_focus(int enable_focus) {
            this.enable_focus = enable_focus;
        }
    }
}
