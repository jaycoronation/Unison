package com.unisonpharmaceuticals.model;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Tushar Vataliya on 28-Jun-18.
 */
public class VariationResponse extends SugarRecord<SugarRecord> {

    /**
     * variations : [{"variation_id":"14","product_id":"2","name":"ALLOUT (PHARMA)","item_code":"ALL","stock":0},{"variation_id":"16","product_id":"12","name":"P.S Naso","item_code":"FPPN002","stock":0},{"variation_id":"15","product_id":"13","name":"P.S. AKILOS-100 TABLETS","item_code":"FPA001PS","stock":0},{"variation_id":"8","product_id":"1","name":"P.S. AKILOS-CR TABLETS","item_code":"FPA104PS","stock":0}]
     * success : 1
     * message : 4 Products found
     */

    private int success = 0;
    private String message = "";
    private List<VariationsBean> variations;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<VariationsBean> getVariations() {
        return variations;
    }

    public void setVariations(List<VariationsBean> variations) {
        this.variations = variations;
    }

    public static class VariationsBean extends SugarRecord<SugarRecord> {
        /**
         * variation_id : 14
         * product_id : 2
         * name : ALLOUT (PHARMA)
         * item_code : ALL
         * stock : 0
         */

        private String variation_id = "";
        private String product_id = "";
        private String name = "";
        private String item_code = "";
        private String reason="Regular Sample";
        private String reason_code="R";
        private String reason_id = "4";
        private String item_id_code = "";
        private String stock = "";
        private boolean isChecked = false;
        private String product_type = "";
        private boolean isTemp = false; // For Check is ivDone click or not, suppose 3 products added bot not clicked on ivDone than these products will be remove when ActivityPendingEntry.java destroyed

        public String getVariation_id() {
            return variation_id;
        }

        public void setVariation_id(String variation_id) {
            this.variation_id = variation_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_code() {
            return item_code;
        }

        public void setItem_code(String item_code) {
            this.item_code = item_code;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getReason_code() {
            return reason_code;
        }

        public void setReason_code(String reason_code) {
            this.reason_code = reason_code;
        }

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getProduct_type()
        {
            return product_type;
        }

        public void setProduct_type(String product_type)
        {
            this.product_type = product_type;
        }

        public String getItem_id_code() {
            return item_id_code;
        }

        public void setItem_id_code(String item_id_code) {
            this.item_id_code = item_id_code;
        }

        public boolean isTemp() {
            return isTemp;
        }

        public void setTemp(boolean temp) {
            isTemp = temp;
        }
    }
}
