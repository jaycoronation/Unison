package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 19-Sep-18.
 */
public class NotificationResponse
{

    /**
     * notifications : [{"notification_id":"10","message":"MANISH A. PARIKH has updated sales for 10379 - DR. HIMANI DOCTOR,MD,DGO from 1400 to 1800","date":"19 September, 2018","time":"15 Minutes ago","is_read":"false"},{"notification_id":"9","message":"MANISH A. PARIKH has updated sales for 10379 - DR. HIMANI DOCTOR,MD,DGO from 1300 to 1400","date":"19 September, 2018","time":"1 Hour, 17 Minutes ago","is_read":"false"},{"notification_id":"8","message":"MANISH A. PARIKH has updated sale for 10379 - DR. HIMANI DOCTOR,MD,DGO from 1260 to 1300","date":"19 September, 2018","time":"1 Hour, 17 Minutes ago","is_read":"false"},{"notification_id":"7","message":"MILIND D. DESAI has approved MANISH A. PARIKH's gift plan for September, 2018","date":"19 September, 2018","time":"1 Hour, 47 Minutes ago","is_read":"false"},{"notification_id":"6","message":"MANISH A. PARIKH has submitted gift plan for September, 2018","date":"19 September, 2018","time":"2 Hours, 1 Minute ago","is_read":"false"},{"notification_id":"4","message":"MANISH A. PARIKH has submitted tour plan for October, 2018","date":"19 September, 2018","time":"3 Hours, 16 Minutes ago","is_read":"false"}]
     * total : 6
     * success : 1
     * message :
     */

    private String total = "";
    private int success = 0;
    private String message = "";
    private List<NotificationsBean> notifications = new ArrayList<>();

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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

    public List<NotificationsBean> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationsBean> notifications) {
        this.notifications = notifications;
    }

    public static class NotificationsBean {
        /**
         * notification_id : 10
         * message : MANISH A. PARIKH has updated sales for 10379 - DR. HIMANI DOCTOR,MD,DGO from 1400 to 1800
         * date : 19 September, 2018
         * time : 15 Minutes ago
         * is_read : false
         */

        private String notification_id = "";
        private String message = "";
        private String date = "";
        private String time = "";
        private String is_read = "";
        private String title = "";
        private String type = "";
        /*private String image = "";*/

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
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

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
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

        /*public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }*/
    }
}
