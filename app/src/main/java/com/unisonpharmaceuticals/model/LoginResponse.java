package com.unisonpharmaceuticals.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tushar Vataliya on 21-Jun-18.
 */
public class LoginResponse {

    /**
     * success : 1
     * message : Successfully logged in
     * admin : {"admin_id":"20","first_name":"MANISH","last_name":"A. PARIKH","username":"map","email":"","phone":"9979894237","territory_code":[{"employee_id":"31","code":"0103010306","designation":"Medical sales representative"}],"user_type":"mr","field_work":0}
     */

    private int success = 0;
    private String message = "";
    private String user_id = "";
    private boolean is_manager_login = false;
    private boolean local_emp_login = true;
    private AdminBean admin = new AdminBean();

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

    public boolean isIs_manager_login() {
        return is_manager_login;
    }

    public void setIs_manager_login(boolean is_manager_login) {
        this.is_manager_login = is_manager_login;
    }

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isLocal_emp_login() {
        return local_emp_login;
    }

    public void setLocal_emp_login(boolean local_emp_login) {
        this.local_emp_login = local_emp_login;
    }

    public static class AdminBean {
        /**
         * admin_id : 20
         * first_name : MANISH
         * last_name : A. PARIKH
         * username : map
         * email :
         * phone : 9979894237
         * territory_code : [{"employee_id":"31","code":"0103010306","designation":"Medical sales representative"}]
         * user_type : mr
         * field_work : 0
         */

        private String admin_id = "";
        private String first_name = "";
        private String last_name = "";
        private String username = "";
        private String email = "";
        private String phone = "";
        private String user_type = "";
        private int field_work = 0;
        private int force_leave = 0;
        private List<TerritoryCodeBean> territory_code = new ArrayList<>();

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public int getField_work() {
            return field_work;
        }

        public void setField_work(int field_work) {
            this.field_work = field_work;
        }

        public int getForce_leave() {
            return force_leave;
        }

        public void setForce_leave(int force_leave) {
            this.force_leave = force_leave;
        }

        public List<TerritoryCodeBean> getTerritory_code() {
            return territory_code;
        }

        public void setTerritory_code(List<TerritoryCodeBean> territory_code) {
            this.territory_code = territory_code;
        }

        public static class TerritoryCodeBean {
            /**
             * employee_id : 31
             * code : 0103010306
             * designation : Medical sales representative
             */

            private String employee_id = "";
            private String division = "";
            private String code = "";
            private String designation = "";
            private boolean selected = false;
            private String user_type = "";
            private String is_sample_entry="";
            private String is_sample_report="";
            private String is_sales_entry="";
            private String is_sales_report="";
            private boolean is_day_end=false;
            private String is_lock_stk = "";
            private String is_stk_done = "";
            private String is_offday = "";

            public String getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(String employee_id) {
                this.employee_id = employee_id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getDesignation() {
                return designation;
            }

            public void setDesignation(String designation) {
                this.designation = designation;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            public String getUser_type() {
                return user_type;
            }

            public void setUser_type(String user_type) {
                this.user_type = user_type;
            }

            public String getIs_sample_entry() {
                return is_sample_entry;
            }

            public void setIs_sample_entry(String is_sample_entry) {
                this.is_sample_entry = is_sample_entry;
            }

            public String getIs_sample_report() {
                return is_sample_report;
            }

            public void setIs_sample_report(String is_sample_report) {
                this.is_sample_report = is_sample_report;
            }

            public String getIs_sales_entry() {
                return is_sales_entry;
            }

            public void setIs_sales_entry(String is_sales_entry) {
                this.is_sales_entry = is_sales_entry;
            }

            public String getIs_sales_report() {
                return is_sales_report;
            }

            public void setIs_sales_report(String is_sales_report) {
                this.is_sales_report = is_sales_report;
            }

            public boolean getIs_day_end() {
                return is_day_end;
            }

            public void setIs_day_end(boolean is_day_end) {
                this.is_day_end = is_day_end;
            }

            public String getDivision() {
                return division;
            }

            public void setDivision(String division) {
                this.division = division;
            }

            public String getIs_lock_stk() {
                return is_lock_stk;
            }

            public void setIs_lock_stk(String is_lock_stk) {
                this.is_lock_stk = is_lock_stk;
            }

            public String getIs_stk_done() {
                return is_stk_done;
            }

            public void setIs_stk_done(String is_stk_done) {
                this.is_stk_done = is_stk_done;
            }

            public String getIs_offday() {
                return is_offday;
            }

            public void setIs_offday(String is_offday) {
                this.is_offday = is_offday;
            }
        }
    }
}
