package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 17-Oct-18.
 */
public class NCREmployeeResponse
{

    private List<DivisionEmployeesBean> division_employees = new ArrayList<>();

    public List<DivisionEmployeesBean> getDivision_employees() {
        return division_employees;
    }

    public void setDivision_employees(List<DivisionEmployeesBean> division_employees) {
        this.division_employees = division_employees;
    }

    public static class DivisionEmployeesBean {
        /**
         * division_id : 6
         * division : Pharma
         * employees : [{"employee_id":"34","employee":"UDAY S. SHAH"},{"employee_id":"35","employee":"PIYUSH B. SHAH"},{"employee_id":"37","employee":"RAVI P. JOSHI"},{"employee_id":"38","employee":"ARVIND D. MIYATRA"},{"employee_id":"39","employee":"SHANIL C. TELEE"},{"employee_id":"168","employee":"HEMANT V. BHADANI"},{"employee_id":"221","employee":"NAVNIT A. BHIMANI"},{"employee_id":"228","employee":"VIPUL P. PATEL"}]
         */

        private String division_id;
        private String division;
        private List<EmployeesBean> employees;

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public List<EmployeesBean> getEmployees() {
            return employees;
        }

        public void setEmployees(List<EmployeesBean> employees) {
            this.employees = employees;
        }

        public static class EmployeesBean {
            /**
             * employee_id : 34
             * employee : UDAY S. SHAH
             */

            private String employee_id;
            private String employee;
            private boolean selected = false;

            public String getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(String employee_id) {
                this.employee_id = employee_id;
            }

            public String getEmployee() {
                return employee;
            }

            public void setEmployee(String employee) {
                this.employee = employee;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }
        }
    }
}
