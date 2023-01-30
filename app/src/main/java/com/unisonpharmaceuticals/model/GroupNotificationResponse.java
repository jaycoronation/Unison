package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 22-Sep-18.
 */
public class GroupNotificationResponse
{

    /**
     * notifications : {"tourPlan":"2","dcr":"0","gift":"0","travelling":"0","notSeen":"0"}
     * total : 2
     * success : 0
     * message :
     */

    private NotificationsBean notifications = new NotificationsBean();
    private int total = 0;
    private int success = 0;
    private String message = "";

    public NotificationsBean getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationsBean notifications) {
        this.notifications = notifications;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public static class NotificationsBean {
        /**
         * tourPlan : 2
         * dcr : 0
         * gift : 0
         * travelling : 0
         * notSeen : 0
         */

        private String tourPlan = "";
        private String dcr = "";
        private String gift = "";
        private String travelling = "";
        private String notSeen = "";
        private String other = "";

        public String getTourPlan() {
            return tourPlan;
        }

        public void setTourPlan(String tourPlan) {
            this.tourPlan = tourPlan;
        }

        public String getDcr() {
            return dcr;
        }

        public void setDcr(String dcr) {
            this.dcr = dcr;
        }

        public String getGift() {
            return gift;
        }

        public void setGift(String gift) {
            this.gift = gift;
        }

        public String getTravelling() {
            return travelling;
        }

        public void setTravelling(String travelling) {
            this.travelling = travelling;
        }

        public String getNotSeen() {
            return notSeen;
        }

        public void setNotSeen(String notSeen) {
            this.notSeen = notSeen;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }
}
