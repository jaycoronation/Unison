package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 30-Jul-18.
 */
public class LeaveResponse
{

    /**
     * leave : {"casual_leave":{"used":"0","total":"0","pending":"0"},"medical_leave":{"used":"0","total":"0","pending":"0"},"privilege_leave":{"used":"0","total":"0","pending":"0"}}
     * success : 1
     * message :
     */

    private LeaveBean leave;
    private int success = 0;
    private String message;

    public LeaveBean getLeave() {
        return leave;
    }

    public void setLeave(LeaveBean leave) {
        this.leave = leave;
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

    public static class LeaveBean {
        /**
         * casual_leave : {"used":"0","total":"0","pending":"0"}
         * medical_leave : {"used":"0","total":"0","pending":"0"}
         * privilege_leave : {"used":"0","total":"0","pending":"0"}
         */

        private CasualLeaveBean casual_leave;
        private MedicalLeaveBean medical_leave;
        private PrivilegeLeaveBean privilege_leave;

        public CasualLeaveBean getCasual_leave() {
            return casual_leave;
        }

        public void setCasual_leave(CasualLeaveBean casual_leave) {
            this.casual_leave = casual_leave;
        }

        public MedicalLeaveBean getMedical_leave() {
            return medical_leave;
        }

        public void setMedical_leave(MedicalLeaveBean medical_leave) {
            this.medical_leave = medical_leave;
        }

        public PrivilegeLeaveBean getPrivilege_leave() {
            return privilege_leave;
        }

        public void setPrivilege_leave(PrivilegeLeaveBean privilege_leave) {
            this.privilege_leave = privilege_leave;
        }

        public static class CasualLeaveBean {
            /**
             * used : 0
             * total : 0
             * pending : 0
             */

            private String used = "0";
            private String total = "0";
            private String pending = "0";

            public String getUsed() {
                return used;
            }

            public void setUsed(String used) {
                this.used = used;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getPending() {
                return pending;
            }

            public void setPending(String pending) {
                this.pending = pending;
            }
        }

        public static class MedicalLeaveBean {
            /**
             * used : 0
             * total : 0
             * pending : 0
             */

            private String used = "0";
            private String total = "0";
            private String pending = "0";

            public String getUsed() {
                return used;
            }

            public void setUsed(String used) {
                this.used = used;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getPending() {
                return pending;
            }

            public void setPending(String pending) {
                this.pending = pending;
            }
        }

        public static class PrivilegeLeaveBean {
            /**
             * used : 0
             * total : 0
             * pending : 0
             */

            private String used = "0";
            private String total = "0";
            private String pending = "0";

            public String getUsed() {
                return used;
            }

            public void setUsed(String used) {
                this.used = used;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getPending() {
                return pending;
            }

            public void setPending(String pending) {
                this.pending = pending;
            }
        }
    }
}
