package com.unisonpharmaceuticals.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Tushar Vataliya on 28-Jun-18.
 */
public class WorkWithResponse
{

    /**
     * staff : [{"staff_id":"10","name":"Kamal Sharma","designation":"MSR"},{"staff_id":"8","name":"Kiran Patel","designation":"MSR"},{"staff_id":"6","name":"Mayur Chatbar","designation":"MSR"},{"staff_id":"5","name":"Nitinbhai Shah","designation":"MD"},{"staff_id":"11","name":"Pratik Kalariya","designation":"ZSM"},{"staff_id":"7","name":"Ravi Patel","designation":"MSR"},{"staff_id":"9","name":"Sanket Thakker","designation":"MSR"}]
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
        @Expose
        private boolean isSelected = false;

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

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
