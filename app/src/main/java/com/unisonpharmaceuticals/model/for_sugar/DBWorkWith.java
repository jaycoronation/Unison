package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

/**
 * Created by Tushar Vataliya on 13-Jul-18.
 */
public class DBWorkWith extends SugarRecord<DBWorkWith>
{
    private String staff_id;
    private String name;
    private String designation;

    public DBWorkWith() {
    }

    public DBWorkWith(String staff_id, String name, String designation) {
        this.staff_id = staff_id;
        this.name = name;
        this.designation = designation;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
