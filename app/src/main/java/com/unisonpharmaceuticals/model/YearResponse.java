package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Kiran Patel on 24-Jul-18.
 */
public class YearResponse
{

    /**
     * yearList : [{"year":2018},{"year":2017},{"year":2016},{"year":2015},{"year":2014},{"year":2013},{"year":2012},{"year":2011},{"year":2010}]
     * current_year : 2018
     * current_month : 07
     * success : 1
     */

    private String current_year = "";
    private String current_month = "";
    private int success = 0;
    private List<YearListBean> yearList;

    public String getCurrent_year() {
        return current_year;
    }

    public void setCurrent_year(String current_year) {
        this.current_year = current_year;
    }

    public String getCurrent_month() {
        return current_month;
    }

    public void setCurrent_month(String current_month) {
        this.current_month = current_month;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<YearListBean> getYearList() {
        return yearList;
    }

    public void setYearList(List<YearListBean> yearList) {
        this.yearList = yearList;
    }

    public static class YearListBean {
        /**
         * year : 2018
         */

        private int year;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
