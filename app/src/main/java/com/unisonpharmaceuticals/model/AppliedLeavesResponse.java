package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Kiran Patel on 02-Aug-18.
 */
public class AppliedLeavesResponse
{

    /**
     * leaves : [{"leave_id":"18","staff_id":"6","leave":"Mayur Chatbar applied for a this is casual leave (casual_leave) on 05 September, 2018","status":"Pending"},{"leave_id":"14","staff_id":"6","leave":"Mayur Chatbar applied for a this is casual leave (causal_leave) from 01 September, 2018 to 04 September, 2018","status":"Pending"},{"leave_id":"9","staff_id":"6","leave":"Mayur Chatbar applied for a test casual leave (causal_leave) from 02 August, 2018 to 06 August, 2018","status":"Pending"}]
     * success : 1
     * message : 3 leave request found
     */

    private int success = 0;
    private String message = "";
    private List<LeavesBean> leaves;

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

    public List<LeavesBean> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<LeavesBean> leaves) {
        this.leaves = leaves;
    }

    public static class LeavesBean {
        /**
         * leave_id : 18
         * staff_id : 6
         * leave : Mayur Chatbar applied for a this is casual leave (casual_leave) on 05 September, 2018
         * status : Pending
         */

        private String leave_id;
        private String staff_id;
        private String leave;
        private String status;

        public String getLeave_id() {
            return leave_id;
        }

        public void setLeave_id(String leave_id) {
            this.leave_id = leave_id;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getLeave() {
            return leave;
        }

        public void setLeave(String leave) {
            this.leave = leave;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
