package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 29-Sep-18.
 */
public class CirclularNotifResponse
{

    /**
     * circular_notice : {"notification_id":"186","title":"Circular notice","type":"","message":"MR. SAURIN N. SHAH has sent you notice : Hello All","date":"29 September, 2018","time":"1 Minute ago","is_read":false}
     * success : 1
     * message : 1 Circular notice has been found
     */

    private CircularNoticeBean circular_notice = new CircularNoticeBean();
    private int success = 0;
    private String message = "";

    public CircularNoticeBean getCircular_notice() {
        return circular_notice;
    }

    public void setCircular_notice(CircularNoticeBean circular_notice) {
        this.circular_notice = circular_notice;
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

    public static class CircularNoticeBean {
        /**
         * notification_id : 186
         * title : Circular notice
         * type :
         * message : MR. SAURIN N. SHAH has sent you notice : Hello All
         * date : 29 September, 2018
         * time : 1 Minute ago
         * is_read : false
         */

        private String notification_id = "";
        private String title = "";
        private String type = "";
        private String message = "";
        private String date = "";
        private String time = "";
        private boolean is_read = false;

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isIs_read() {
            return is_read;
        }

        public void setIs_read(boolean is_read) {
            this.is_read = is_read;
        }
    }
}
