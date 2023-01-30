package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 27-Jul-18.
 */
public class DrBusinessResponse
{

    /**
     * doctor_business : {"business":"","old_business":""}
     * itmes : [{"qty":0,"item_id_code":"ACR","variation_id":"8"},{"qty":0,"item_id_code":"AER","variation_id":"16"},{"qty":0,"item_id_code":"AK1","variation_id":"15"}]
     * success : 1
     */

    private DoctorBusinessBean doctor_business = new DoctorBusinessBean();
    private int success = 0;
    private List<ItmesBean> itmes = new ArrayList<>();

    public DoctorBusinessBean getDoctor_business() {
        return doctor_business;
    }

    public void setDoctor_business(DoctorBusinessBean doctor_business) {
        this.doctor_business = doctor_business;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<ItmesBean> getItmes() {
        return itmes;
    }

    public void setItmes(List<ItmesBean> itmes) {
        this.itmes = itmes;
    }

    public static class DoctorBusinessBean {
        /**
         * business :
         * old_business :
         */

        private String business;
        private String old_business;
        private double pts_business = 0;

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getOld_business() {
            return old_business;
        }

        public void setOld_business(String old_business) {
            this.old_business = old_business;
        }

        public double getPts_business() {
            return pts_business;
        }

        public void setPts_business(double pts_business) {
            this.pts_business = pts_business;
        }
    }

    public static class ItmesBean {
        /**
         * qty : 0
         * item_id_code : ACR
         * variation_id : 8
         */

        private int qty;
        private String item_id_code = "";
        private String variation_id = "";
        private String name = "";
        private String pts_price = "0";

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getItem_id_code() {
            return item_id_code;
        }

        public void setItem_id_code(String item_id_code) {
            this.item_id_code = item_id_code;
        }

        public String getVariation_id() {
            return variation_id;
        }

        public void setVariation_id(String variation_id) {
            this.variation_id = variation_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPts_price() {
            return pts_price;
        }

        public void setPts_price(String pts_price) {
            this.pts_price = pts_price;
        }
    }
}
