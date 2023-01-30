package com.unisonpharmaceuticals.model.for_sugar;

import java.util.List;

/**
 * Created by Kiran Patel on 02-Aug-18.
 */
public class DisplayReports
{

    /**
     * data : [{"report":"Tour Plan Report","url":"http://192.168.50.10/unison/reports/tourplan.php","params":["employee","year"]}]
     * success : 1
     * message : 1
     */

    private int success = 0;
    private int message = 0;
    private List<DataBean> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * report : Tour Plan Report
         * url : http://192.168.50.10/unison/reports/tourplan.php
         * params : ["employee","year"]
         */

        private String report;
        private String url;
        private List<String> params;

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }
    }
}
