package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 29-Sep-18.
 */
public class SalesNotifResponse
{

    private String message = "";
    private int success = 0;
    private List<NotificationBean> notification = new ArrayList<>();

    public List<NotificationBean> getNotification() {
        return notification;
    }

    public void setNotification(List<NotificationBean> notification) {
        this.notification = notification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public static class NotificationBean {
        /**
         * notification_id : 15
         * doctor : DR. HIMANI DOCTOR,MD,DGO
         * employee : MILIND D. DESAI
         * date : 29 September, 2018
         * old_business :
         * new_business :
         * samples : [{"item":"DI1","old_qty":"0","new_qty":"12"}]
         */

        private String notification_id = "";
        private String doctor = "";
        private String employee = "";
        private String date = "";
        private String old_business = "";
        private String new_business = "";
        private List<SamplesBean> samples = new ArrayList<>();

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getOld_business() {
            return old_business;
        }

        public void setOld_business(String old_business) {
            this.old_business = old_business;
        }

        public String getNew_business() {
            return new_business;
        }

        public void setNew_business(String new_business) {
            this.new_business = new_business;
        }

        public List<SamplesBean> getSamples() {
            return samples;
        }

        public void setSamples(List<SamplesBean> samples) {
            this.samples = samples;
        }

        public static class SamplesBean {
            /**
             * item : DI1
             * old_qty : 0
             * new_qty : 12
             */

            private String item = "";
            private String old_qty = "";
            private String new_qty = "";

            public String getItem() {
                return item;
            }

            public void setItem(String item) {
                this.item = item;
            }

            public String getOld_qty() {
                return old_qty;
            }

            public void setOld_qty(String old_qty) {
                this.old_qty = old_qty;
            }

            public String getNew_qty() {
                return new_qty;
            }

            public void setNew_qty(String new_qty) {
                this.new_qty = new_qty;
            }
        }
    }
}
