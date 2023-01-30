package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 04-Jan-19.
 */
public class DrDcrGetSet
{
    private String drName = "";
    private String drId = "";
    private String reportType = "";

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDrId() {
        return drId;
    }

    public void setDrId(String drId) {
        this.drId = drId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
