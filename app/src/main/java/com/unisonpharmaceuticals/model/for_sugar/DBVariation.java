package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

public class DBVariation extends SugarRecord<DBVariation> {
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
    private String item_id_code = "";
    private String reason = "";
    private String reason_code = "";
    private String reason_id = "";
    private String stock = "";
    private boolean isChecked = false;
    private String product_type = "";

    public DBVariation(String variation_id,
                       String product_id,
                       String name,
                       String item_code,
                       String reason,
                       String reason_code,
                       String stock,
                       boolean isChecked,
                       String reasonId,
                       String product_type,
                       String item_id_code) {
        this.variation_id = variation_id;
        this.product_id = product_id;
        this.name = name;
        this.item_code = item_code;
        this.reason = reason;
        this.reason_code = reason_code;
        this.stock = stock;
        this.isChecked = isChecked;
        this.reason_id = reasonId;
        this.product_type = product_type;
        this.item_id_code = item_id_code;
    }


    public DBVariation(String variation_id,
                       String product_id,
                       String name,
                       String item_code,
                       String reason,
                       String reason_code,
                       String stock,
                       boolean isChecked,
                       String productT) {
        this.variation_id = variation_id;
        this.product_id = product_id;
        this.name = name;
        this.item_code = item_code;
        this.reason = reason;
        this.reason_code = reason_code;
        this.stock = stock;
        this.isChecked = isChecked;
    }


    public DBVariation() {
    }

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

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getItem_id_code() {
        return item_id_code;
    }

    public void setItem_id_code(String item_id_code) {
        this.item_id_code = item_id_code;
    }
}