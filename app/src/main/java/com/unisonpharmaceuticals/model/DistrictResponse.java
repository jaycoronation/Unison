package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 06-Jul-18.
 */
public class DistrictResponse
{

    /**
     * total : 43
     * success : 1
     * message :
     * districts : [{"disctrict_id":"6","district":"Ahmedabad","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"10","district":"Amreli","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"20","district":"Anand","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"35","district":"Ankleshwar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"19","district":"Arvalli","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"9","district":"Banaskantha","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"7","district":"Bardoli","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"17","district":"Baroda","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"13","district":"Bharuch","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"8","district":"Bhavnagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"38","district":"Botad","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"41","district":"Chhota udaipur","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"42","district":"Dadra nagar haveli","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"27","district":"Dahod","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"29","district":"Dang","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"34","district":"Devbhumi dwarka","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"31","district":"Diu","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"4","district":"Gandhinagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"32","district":"Himmatnagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"21","district":"Jamnagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"18","district":"Junagadh","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"11","district":"Kheda","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"5","district":"Kutch","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"39","district":"Mahisagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"37","district":"Mehmdavad","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"2","district":"Mehsana","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"30","district":"Narmada","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"15","district":"Navsari","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"24","district":"Null","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"33","district":"Panchmahal","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"14","district":"Patan","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"22","district":"Porbandar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"3","district":"Rajkot","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"12","district":"Sabarkantha","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"40","district":"Sabrakantha","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"16","district":"Surat","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"25","district":"Surendranagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"43","district":"Tapi","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"28","district":"Valsad","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"23","district":"Vapi","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"44","district":"Vijapur","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"26","district":"Visnagar","state_id":"12","state":"Gujarat","country_id":"101","country":"India"},{"disctrict_id":"36","district":"Wankaner","state_id":"12","state":"Gujarat","country_id":"101","country":"India"}]
     */

    private String total = "";
    private int success = 0;
    private String message = "";
    private List<DistrictsBean> districts;

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

    public List<DistrictsBean> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictsBean> districts) {
        this.districts = districts;
    }

    public static class DistrictsBean {
        /**
         * disctrict_id : 6
         * district : Ahmedabad
         * state_id : 12
         * state : Gujarat
         * country_id : 101
         * country : India
         */

        private String disctrict_id = "";
        private String district = "";
        private String state_id = "";
        private String state = "";
        private String country_id = "";
        private String country = "";
        private boolean selected = false;

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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
