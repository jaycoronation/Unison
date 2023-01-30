package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 29-Sep-18.
 */
public class EmployeeSalesNotifResponse
{

    /**
     * staff : [{"employee":"MANISH A. PARIKH ","employee_id":"172"}]
     * success : 1
     * message :
     */

    private int success = 0;
    private String message = "";
    private List<StaffBean> staff  = new ArrayList<>();

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
         * employee : MANISH A. PARIKH
         * employee_id : 172
         */

        private String employee = "";
        private String employee_id = "";

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }
    }
}
