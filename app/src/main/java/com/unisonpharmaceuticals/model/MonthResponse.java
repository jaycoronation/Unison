package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Kiran Patel on 11-Aug-18.
 */
public class MonthResponse
{

    private List<MonthListBean> monthList;

    public List<MonthListBean> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<MonthListBean> monthList) {
        this.monthList = monthList;
    }

    public static class MonthListBean {
        /**
         * number : 01
         * month : January
         * type : Past
         */

        private String number = "";
        private String month = "";
        private String type = "";

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
