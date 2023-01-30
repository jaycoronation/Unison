package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

/**
 * Created by Tushar Vataliya on 13-Jul-18.
 */
public class DBReports extends SugarRecord<DBReports>
{
    private String report_name;
    private String report_code;
    private String report_id;

    public DBReports() {
    }

    public DBReports(String report_name, String report_code, String report_id) {
        this.report_name = report_name;
        this.report_code = report_code;
        this.report_id = report_id;
    }

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
