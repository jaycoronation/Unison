package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 06-Jul-18.
 */
public class CityResponse
{

    /**
     * total : 18
     * success : 1
     * message :
     * cities : [{"city_id":"1","name":"Aaithor","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"9","name":"Ambaliyasan","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"10","name":"Ambasan","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"137","name":"Jotana","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"139","name":"Kadi","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"161","name":"Kheralu","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"162","name":"Kherva","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"170","name":"Kukarwada","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"175","name":"Ladol","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"209","name":"Mehsana","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"225","name":"Nandasan","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"282","name":"Satlasana","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"301","name":"Sundhiya","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"320","name":"Udalpur","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"327","name":"Unjha","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"335","name":"Vadnagar","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"353","name":"Vijapur","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"city_id":"360","name":"Visnagar","disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"}]
     */

    private String total;
    private int success;
    private String message;
    private List<CitiesBean> cities;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public List<CitiesBean> getCities() {
        return cities;
    }

    public void setCities(List<CitiesBean> cities) {
        this.cities = cities;
    }

    public static class CitiesBean {
        /**
         * city_id : 1
         * name : Aaithor
         * disctrict_id : 2
         * district : Mehsana
         * state_id : 12
         * state : Gujarat
         * country_id : 101
         * country : India
         */

        private String city_id = "";
        private String name = "";
        private String disctrict_id = "";
        private String district = "";
        private String state_id = "";
        private String state = "";
        private String country_id = "";
        private String country = "";

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisctrict_id() {
            return disctrict_id;
        }

        public void setDisctrict_id(String disctrict_id) {
            this.disctrict_id = disctrict_id;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
