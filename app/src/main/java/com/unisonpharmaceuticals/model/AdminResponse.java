package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 24-Aug-18.
 */
public class AdminResponse
{

    /**
     * admin : {"employee_id":"31","user_id":"20","first_name":"MANISH","last_name":"A. PARIKH","division_id":"8","employee_code":"map","designation":"Medical sales representative","designation_code":"MSR","designation_id":"8","headquarter_id":"10","headquarter_code":"0103010306","headquarter":"SURATH7","field_work":1}
     * success : 1
     * message :
     */

    private AdminBean admin = new AdminBean();
    private int success = 0;
    private String message = "";

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
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

    public static class AdminBean {
        /**
         * employee_id : 31
         * user_id : 20
         * first_name : MANISH
         * last_name : A. PARIKH
         * division_id : 8
         * employee_code : map
         * designation : Medical sales representative
         * designation_code : MSR
         * designation_id : 8
         * headquarter_id : 10
         * headquarter_code : 0103010306
         * headquarter : SURATH7
         * field_work : 1
         */

        private String employee_id = "";
        private String user_id = "";
        private String first_name = "";
        private String last_name = "";
        private String division_id = "";
        private String employee_code = "";
        private String designation = "";
        private String designation_code = "";
        private String designation_id = "";
        private String headquarter_id = "";
        private String headquarter_code = "";
        private String headquarter = "";
        private int field_work = 0;

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
        }

        public String getEmployee_code() {
            return employee_code;
        }

        public void setEmployee_code(String employee_code) {
            this.employee_code = employee_code;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDesignation_code() {
            return designation_code;
        }

        public void setDesignation_code(String designation_code) {
            this.designation_code = designation_code;
        }

        public String getDesignation_id() {
            return designation_id;
        }

        public void setDesignation_id(String designation_id) {
            this.designation_id = designation_id;
        }

        public String getHeadquarter_id() {
            return headquarter_id;
        }

        public void setHeadquarter_id(String headquarter_id) {
            this.headquarter_id = headquarter_id;
        }

        public String getHeadquarter_code() {
            return headquarter_code;
        }

        public void setHeadquarter_code(String headquarter_code) {
            this.headquarter_code = headquarter_code;
        }

        public String getHeadquarter() {
            return headquarter;
        }

        public void setHeadquarter(String headquarter) {
            this.headquarter = headquarter;
        }

        public int getField_work() {
            return field_work;
        }

        public void setField_work(int field_work) {
            this.field_work = field_work;
        }
    }
}
