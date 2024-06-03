package com.unisonpharmaceuticals.model;


import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

/**
 * Created by Tushar Vataliya on 02-Jul-18.
 */
public class NewEntryGetSet extends SugarRecord<NewEntryGetSet>
{
    private String user_id = "";
    private String area = "";
    private String area_id = "";
    private String speciality = "";
    private String speciality_id = "";
    private String report_type = "";
    private String work_with = "";
    private String work_with_id = "";
    private String dr="";
    private String dr_id = "";
    private String new_cycle = "";
    private String remark = "";
    private String remark_type = "";
    private String internee = "";
    private String products = "";
    private String focusProducts = "";
    private boolean isEditable = false;
    private String ddtdate	= "ddtdate";
    private String empId="";
    private String empName="";
    private String advDate = "";
    private String advice = "";
    private boolean isWorkWithSelf = true;
    private String NCRDrData = "";


    public NewEntryGetSet(String area,
                          String area_id,
                          String speciality,
                          String speciality_id,
                          String report_type,
                          String work_with,
                          String work_with_id,
                          String dr,
                          String dr_id,
                          String new_cycle,
                          String remark,
                          String remark_type,
                          String internee,
                          String products,
                          String focusProducts,
                          boolean isWorkWithSelf,
                          String timeStamp,
                          String ncrDrData,
                          String empId,
                          String empName,
                          String advDate,
                          String advice,
                          String user_id) {
        this.area = area;
        this.area_id = area_id;
        this.speciality = speciality;
        this.speciality_id = speciality_id;
        this.report_type = report_type;
        this.work_with = work_with;
        this.work_with_id = work_with_id;
        this.dr = dr;
        this.dr_id = dr_id;
        this.new_cycle = new_cycle;
        this.remark = remark;
        this.remark_type = remark_type;
        this.internee = internee;
        this.products= products;
        this.focusProducts = focusProducts;
        this.isWorkWithSelf = isWorkWithSelf;
        this.ddtdate = timeStamp;
        this.NCRDrData = ncrDrData;
        this.empId = empId;
        this.empName = empName;
        this.advDate = advDate;
        this.advice = advice;
        this.user_id = user_id;
    }

    public NewEntryGetSet()
    {

    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(String speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getWork_with() {
        return work_with;
    }

    public void setWork_with(String work_with) {
        this.work_with = work_with;
    }

    public String getWork_with_id() {
        return work_with_id;
    }

    public void setWork_with_id(String work_with_id) {
        this.work_with_id = work_with_id;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getDr_id() {
        return dr_id;
    }

    public void setDr_id(String dr_id) {
        this.dr_id = dr_id;
    }

    public String getNew_cycle() {
        return new_cycle;
    }

    public void setNew_cycle(String new_cycle) {
        this.new_cycle = new_cycle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark_type() {
        return remark_type;
    }

    public void setRemark_type(String remark_type) {
        this.remark_type = remark_type;
    }

    public String getInternee() {
        return internee;
    }

    public void setInternee(String internee) {
        this.internee = internee;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getFocusProducts() {
        return focusProducts;
    }

    public void setFocusProducts(String focusProducts) {
        this.focusProducts = focusProducts;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public String getDdtdate() {
        return ddtdate;
    }
    public void setDdtdate(String ddtdate) {
        this.ddtdate = ddtdate;
    }

    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public boolean isWorkWithSelf() {
        return isWorkWithSelf;
    }

    public void setWorkWithSelf(boolean workWithSelf) {
        isWorkWithSelf = workWithSelf;
    }

    public String getNCRDrData() {
        return NCRDrData;
    }

    public void setNCRDrData(String NCRDrData) {
        this.NCRDrData = NCRDrData;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAdvDate() {
        return advDate;
    }

    public void setAdvDate(String advDate) {
        this.advDate = advDate;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
