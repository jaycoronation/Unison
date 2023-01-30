package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Kiran Patel on 24-Jul-18.
 */
public class TourAreaResponse
{

    /**
     * tour_area : [{"tp_name":"mandvi MR","area_name":"Durgapur","area_id":"709"},{"tp_name":"mandvi MR","area_name":"Gadhshisha","area_id":"333"},{"tp_name":"ahmedabad  MR","area_name":"Astodia","area_id":"773"},{"tp_name":"ahmedabad  MR","area_name":"Babra","area_id":"821"},{"tp_name":"ahmedabad  MR","area_name":"Randeja","area_id":"643"},{"tp_name":"ahmedabad  MR","area_name":"Sarangpur","area_id":"602"}]
     * success : 1
     */

    private int success = 0;
    private List<TourAreaBean> tour_area;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<TourAreaBean> getTour_area() {
        return tour_area;
    }

    public void setTour_area(List<TourAreaBean> tour_area) {
        this.tour_area = tour_area;
    }

    public static class TourAreaBean {
        /**
         * tp_name : mandvi MR
         * area_name : Durgapur
         * area_id : 709
         */

        private String tp_name = "";
        private String area_name = "";
        private String area_id = "";
        private String route_area_id = "";

        public String getTp_name() {
            return tp_name;
        }

        public void setTp_name(String tp_name) {
            this.tp_name = tp_name;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getRoute_area_id() {
            return route_area_id;
        }

        public void setRoute_area_id(String route_area_id) {
            this.route_area_id = route_area_id;
        }
    }
}
