package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 28-Mar-19.
 */
public class DoctorDetailsResponse
{

    /**
     * divisions : [{"division":"Harmony","employee":"JATIN R. OZA ","business":"11900","last_visit":"16/01/2019","samples":[{"item":"FRS","quantity":"2"},{"item":"MAM","quantity":"3"},{"item":"ML2","quantity":"3"},{"item":"ML5","quantity":"3"},{"item":"MSL","quantity":"3"},{"item":"NM2","quantity":"5"},{"item":"NMP","quantity":"5"},{"item":"TS1","quantity":"3"},{"item":"TS2","quantity":"3"},{"item":"TS4","quantity":"3"},{"item":"TSF","quantity":"3"}],"sales":[{"item":"MAM","quantity":"3"},{"item":"ML5","quantity":"3"},{"item":"MSL","quantity":"5"},{"item":"TS1","quantity":"5"},{"item":"TS2","quantity":"4"},{"item":"TS4","quantity":"5"}]}]
     * doctor : {"name":"DR. JAVED SHAIKH,MD","speciality":"PHYSICIAN","qualification":"MD","area":"Mehsana","city":"Mehsana","business":22200}
     */

    private DoctorBean doctor;
    private List<DivisionsBean> divisions = new ArrayList<>();

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public List<DivisionsBean> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<DivisionsBean> divisions) {
        this.divisions = divisions;
    }

    public static class DoctorBean {
        /**
         * name : DR. JAVED SHAIKH,MD
         * speciality : PHYSICIAN
         * qualification : MD
         * area : Mehsana
         * city : Mehsana
         * business : 22200
         */

        private String name = "";
        private String speciality = "";
        private String qualification = "";
        private String area = "";
        private String city = "";
        private int business = 0;
        private String address_1 = "";
        private String address_2 = "";
        private String address_3 = "";
        private String address_4 = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getBusiness() {
            return business;
        }

        public void setBusiness(int business) {
            this.business = business;
        }

        public String getAddress_1() {
            return address_1;
        }

        public void setAddress_1(String address_1) {
            this.address_1 = address_1;
        }

        public String getAddress_2() {
            return address_2;
        }

        public void setAddress_2(String address_2) {
            this.address_2 = address_2;
        }

        public String getAddress_3() {
            return address_3;
        }

        public void setAddress_3(String address_3) {
            this.address_3 = address_3;
        }

        public String getAddress_4() {
            return address_4;
        }

        public void setAddress_4(String address_4) {
            this.address_4 = address_4;
        }
    }

    public static class DivisionsBean {
        /**
         * division : Harmony
         * employee : JATIN R. OZA
         * business : 11900
         * last_visit : 16/01/2019
         * samples : [{"item":"FRS","quantity":"2"},{"item":"MAM","quantity":"3"},{"item":"ML2","quantity":"3"},{"item":"ML5","quantity":"3"},{"item":"MSL","quantity":"3"},{"item":"NM2","quantity":"5"},{"item":"NMP","quantity":"5"},{"item":"TS1","quantity":"3"},{"item":"TS2","quantity":"3"},{"item":"TS4","quantity":"3"},{"item":"TSF","quantity":"3"}]
         * sales : [{"item":"MAM","quantity":"3"},{"item":"ML5","quantity":"3"},{"item":"MSL","quantity":"5"},{"item":"TS1","quantity":"5"},{"item":"TS2","quantity":"4"},{"item":"TS4","quantity":"5"}]
         */

        private String division = "";
        private String employee = "";
        private String business = "";
        private String last_visit = "";
        private List<SamplesBean> samples = new ArrayList<>();
        private List<SalesBean> sales = new ArrayList<>();

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getLast_visit() {
            return last_visit;
        }

        public void setLast_visit(String last_visit) {
            this.last_visit = last_visit;
        }

        public List<SamplesBean> getSamples() {
            return samples;
        }

        public void setSamples(List<SamplesBean> samples) {
            this.samples = samples;
        }

        public List<SalesBean> getSales() {
            return sales;
        }

        public void setSales(List<SalesBean> sales) {
            this.sales = sales;
        }

        public static class SamplesBean {
            /**
             * item : FRS
             * quantity : 2
             */

            private String item = "";
            private String quantity = "";

            public String getItem() {
                return item;
            }

            public void setItem(String item) {
                this.item = item;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }
        }

        public static class SalesBean {
            /**
             * item : MAM
             * quantity : 3
             */

            private String item = "";
            private String quantity = "";

            public String getItem() {
                return item;
            }

            public void setItem(String item) {
                this.item = item;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }
        }
    }
}
