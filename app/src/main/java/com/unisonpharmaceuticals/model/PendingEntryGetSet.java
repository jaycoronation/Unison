package com.unisonpharmaceuticals.model;

import java.util.ArrayList;

public class PendingEntryGetSet 
{
	private String id = "";
	String date = "";
	String area = "";
	String dbc = "";
	String dbccode = "";
	String workwith = "workwith";
	String workwithcode = "workwithcode";
	String doctor = "doctor";
	String doctorcode = "doctorcode";
	String newdoctoramount = "newdoctoramount";
	String newdoctorcode = "newdoctorcode";
	String remarks	= "remarks";
	String internee	= "internee";
	String ddtdate	= "ddtdate";
	String isnewcycle = "isnewcycle";
	String product	= "product";
	String reason = "reason";
	String unit = "unit";
	String empId="";
	String empName="";
	String doctorspeciality="";
	String focusForString="";
	String doctorType = "";

	public String getDoctorType() {
		return doctorType;
	}
	public void setDoctorType(String doctorType) {
		this.doctorType = doctorType;
	}
	boolean isEditable = false;

	public String getDoctorspeciality() {
		return doctorspeciality;
	}
	public void setDoctorspeciality(String doctorspeciality) {
		this.doctorspeciality = doctorspeciality;
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
	public boolean isEditable() {
		return isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	private ArrayList<ViewEntryChildGetSet> listDashborad;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDbc() {
		return dbc;
	}
	public void setDbc(String dbc) {
		this.dbc = dbc;
	}
	public String getDbccode() {
		return dbccode;
	}
	public void setDbccode(String dbccode) {
		this.dbccode = dbccode;
	}
	public String getWorkwith() {
		return workwith;
	}
	public void setWorkwith(String workwith) {
		this.workwith = workwith;
	}
	public String getWorkwithcode() {
		return workwithcode;
	}
	public void setWorkwithcode(String workwithcode) {
		this.workwithcode = workwithcode;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getDoctorcode() {
		return doctorcode;
	}
	public void setDoctorcode(String doctorcode) {
		this.doctorcode = doctorcode;
	}
	public String getNewdoctoramount() {
		return newdoctoramount;
	}
	public void setNewdoctoramount(String newdoctoramount) {
		this.newdoctoramount = newdoctoramount;
	}
	public String getNewdoctorcode() {
		return newdoctorcode;
	}
	public void setNewdoctorcode(String newdoctorcode) {
		this.newdoctorcode = newdoctorcode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getInternee() {
		return internee;
	}
	public void setInternee(String internee) {
		this.internee = internee;
	}
	public String getDdtdate() {
		return ddtdate;
	}
	public void setDdtdate(String ddtdate) {
		this.ddtdate = ddtdate;
	}
	public String getIsnewcycle() {
		return isnewcycle;
	}
	public void setIsnewcycle(String isnewcycle) {
		this.isnewcycle = isnewcycle;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<ViewEntryChildGetSet> getListDashborad() {
		return listDashborad;
	}
	public void setListDashborad(ArrayList<ViewEntryChildGetSet> listDashborad) {
		this.listDashborad = listDashborad;
	}

	public String getFocusForString() {
		return focusForString;
	}

	public void setFocusForString(String focusForString) {
		this.focusForString = focusForString;
	}
}
