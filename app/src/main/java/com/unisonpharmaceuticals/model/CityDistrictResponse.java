package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 26-Jan-19.
 */
public class CityDistrictResponse
{

    /**
     * area : {"area_id":"396","area":"Essar","city_id":"302","city":"Surat","district_id":"16","district":"Surat"}
     * success : 1
     * message :
     */

    private AreaBean area;
    private int success = 0;
    private String message = "";

    public AreaBean getArea() {
        return area;
    }

    public void setArea(AreaBean area) {
        this.area = area;
    }

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

    public static class AreaBean {
        /**
         * area_id : 396
         * area : Essar
         * city_id : 302
         * city : Surat
         * district_id : 16
         * district : Surat
         */

        private String area_id = "";
        private String area = "";
        private String city_id = "";
        private String city = "";
        private String district_id = "";
        private String district = "";

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

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(String district_id) {
            this.district_id = district_id;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }
}
