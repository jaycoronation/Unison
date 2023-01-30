package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 27-Jun-18.
 */
public class ReportResponse
{

    /**
     * success : 1
     * reports : [{"report_name":"Assistant Doctor Report","report_code":"ACR","report_id":"1"},{"report_name":"Day End","report_code":"DAYEND","report_id":"2"},{"report_name":"Daily Call Report","report_code":"DCR","report_id":"3"},{"report_name":"Internee Doctor","report_code":"INT","report_id":"4"},{"report_name":"Doctor Joint Visit","report_code":"JCR","report_id":"5"},{"report_name":"Locum Call Report","report_code":"LCR","report_id":"6"},{"report_name":"New Doctor Call","report_code":"NCR","report_id":"7"},{"report_name":"Remark","report_code":"RMK","report_id":"8"},{"report_name":"Rotation Assistant","report_code":"ROA","report_id":"9"},{"report_name":"Resident On Rotation","report_code":"ROR","report_id":"10"},{"report_name":"Sample Refused By Doctor","report_code":"SRD","report_id":"11"},{"report_name":"For Closing Stock","report_code":"STK","report_id":"12"},{"report_name":"Taking Not Sample","report_code":"TNS","report_id":"13"},{"report_name":"Doctor Not Meet but Sample Given","report_code":"XCR","report_id":"14"},{"report_name":"Reminder Call","report_code":"ZCR","report_id":"15"}]
     */

    private int success;
    private List<ReportsBean> reports;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<ReportsBean> getReports() {
        return reports;
    }

    public void setReports(List<ReportsBean> reports) {
        this.reports = reports;
    }

    public static class ReportsBean {
        /**
         * report_name : Assistant Doctor Report
         * report_code : ACR
         * report_id : 1
         */

        private String report_name;
        private String report_code;
        private String report_id;

        public String getReport_name() {
            return report_name;
        }

        public void setReport_name(String report_name) {
            this.report_name = report_name;
        }

        public String getReport_code() {
            return report_code;
        }

        public void setReport_code(String report_code) {
            this.report_code = report_code;
        }

        public String getReport_id() {
            return report_id;
        }

        public void setReport_id(String report_id) {
            this.report_id = report_id;
        }
    }
}
