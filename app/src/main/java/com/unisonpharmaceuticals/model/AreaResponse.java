package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 27-Jun-18.
 */
public class AreaResponse
{

    /**
     * success : 1
     * areas : [{"area_id":"870","area":"V. s. hospital"},{"area_id":"167","area":"Siddhpur"},{"area_id":"101","area":"Paldi"}]
     */

    private int success = 0;
    private String message = "";
    private List<AreasBean> areas;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<AreasBean> getAreas() {
        return areas;
    }

    public void setAreas(List<AreasBean> areas) {
        this.areas = areas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class AreasBean {
        /**
         * area_id : 870
         * area : V. s. hospital
         */

        private String area_id = "";
        private String area = "";
        private boolean selected = false;
        private String is_tour_plan = "";

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

        public String getIs_tour_plan() {
            return is_tour_plan;
        }

        public void setIs_tour_plan(String is_tour_plan) {
            this.is_tour_plan = is_tour_plan;
        }
    }
}
