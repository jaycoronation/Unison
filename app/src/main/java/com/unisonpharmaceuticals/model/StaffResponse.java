package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 09-Jul-18.
 */
public class StaffResponse
{

    /**
     * staff : [{"staff_id":"10","name":"Kamal Sharma","designation":"MSR"},{"staff_id":"6","name":"Mayur Chatbar","designation":"MSR"}]
     * success : 1
     * message :
     */

    private int success;
    private String message;
    private List<StaffBean> staff;

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
         * staff_id : 10
         * name : Kamal Sharma
         * designation : MSR
         */

        private String staff_id;
        private String name;
        private String designation;

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }
    }
}
