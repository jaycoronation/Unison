package com.unisonpharmaceuticals.model;

import java.util.ArrayList;

public class ViewEntryChildGetSet 
{
	private String product = "";
	private String quantity = "";
	private String reason = "";
	private String rdcProduct="";
	private String rdcReason="";
	private String productOne = "";
	private String reasonOne = "";
	private String productTwo = "";
	private String reasonTwo = "";
	
	private ArrayList<FocusForGetSet> listFocusFor = new ArrayList<FocusForGetSet>();

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public ArrayList<FocusForGetSet> getListFocusFor() {
		return listFocusFor;
	}

	public void setListFocusFor(ArrayList<FocusForGetSet> listFocusFor) {
		this.listFocusFor = listFocusFor;
	}

	public String getReason() {
		return reason;
	}

	public String getProductOne() {
		return productOne;
	}

	public void setProductOne(String productOne) {
		this.productOne = productOne;
	}

	public String getReasonOne() {
		return reasonOne;
	}

	public void setReasonOne(String reasonOne) {
		this.reasonOne = reasonOne;
	}

	public String getProductTwo() {
		return productTwo;
	}

	public void setProductTwo(String productTwo) {
		this.productTwo = productTwo;
	}

	public String getReasonTwo() {
		return reasonTwo;
	}

	public void setReasonTwo(String reasonTwo) {
		this.reasonTwo = reasonTwo;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRdcProduct() {
		return rdcProduct;
	}

	public void setRdcProduct(String rdcProduct) {
		this.rdcProduct = rdcProduct;
	}

	public String getRdcReason() {
		return rdcReason;
	}

	public void setRdcReason(String rdcReason) {
		this.rdcReason = rdcReason;
	}
}
     