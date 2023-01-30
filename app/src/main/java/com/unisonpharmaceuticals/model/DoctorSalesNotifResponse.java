package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 29-Sep-18.
 */
public class DoctorSalesNotifResponse
{

    /**
     * staff : [{"doctor":"DR. HIMANI DOCTOR,MD,DGO","doctor_id":null}]
     * success : 1
     * message :
     */

    private int success = 0;
    private String message = "";
    private List<StaffBean> staff = new ArrayList<>();

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

    public List<StaffBean> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffBean> staff) {
        this.staff = staff;
    }

    public static class StaffBean {
        /**
         * doctor : DR. HIMANI DOCTOR,MD,DGO
         * doctor_id : null
         */

        private String doctor = "";
        private String doctor_id = "";

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
    }
}
