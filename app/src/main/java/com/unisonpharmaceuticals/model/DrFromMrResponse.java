package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 27-Jul-18.
 */
public class DrFromMrResponse
{

    /**
     * doctor : [{"doctor_name":"Dr. a.m. thukka","qualification":"MS","doctor_id":"5"},{"doctor_name":"Semil Shah","qualification":"PSY","doctor_id":"11"},{"doctor_name":"chhatbar mayur","qualification":"PSY","doctor_id":"12"},{"doctor_name":"chhatbar mayur","qualification":"PSY","doctor_id":"13"},{"doctor_name":"Doctor2","qualification":"RMO","doctor_id":"15"},{"doctor_name":"Doctor4","qualification":"RMO","doctor_id":"17"},{"doctor_name":"Doctor5","qualification":"RMO","doctor_id":"18"},{"doctor_name":"Doctor6","qualification":"RMO","doctor_id":"19"},{"doctor_name":"Doctor7","qualification":"RMO","doctor_id":"20"}]
     * success : 1
     * message :
     */

    private int success = 0;
    private String message = "";
    private List<DoctorBean> doctor = new ArrayList<>();

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

    public List<DoctorBean> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<DoctorBean> doctor) {
        this.doctor = doctor;
    }

    public static class DoctorBean {
        /**
         * doctor_name : Dr. a.m. thukka
         * qualification : MS
         * doctor_id : 5
         */

        private String doctor_name;
        private String qualification;
        private String doctor_id;

        public String getDoctor_name() {
            return doctor_name;
        }

        public void setDoctor_name(String doctor_name) {
            this.doctor_name = doctor_name;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getDoctor_id() {
            return doctor_id;
        }

        public void setDoctor_id(String doctor_id) {
            this.doctor_id = doctor_id;
        }
    }
}
