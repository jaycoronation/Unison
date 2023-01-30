package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

public class DBArea extends SugarRecord<DBArea> {
    /**
     * area_id : 870
     * area : V. s. hospital
     */

    private String area_id;
    private String area;
    private boolean selected = false;

    public DBArea(){}

    public DBArea(String area_id, String area) {
        this.area_id = area_id;
        this.area = area;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}