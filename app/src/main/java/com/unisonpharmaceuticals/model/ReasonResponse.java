package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 29-Jun-18.
 */
public class ReasonResponse
{

    /**
     * total : 9
     * success : 1
     * message :
     * reasons : [{"reason_id":"12","reason":"Test","reason_code":"TEST","comment":"test","timestamp":"19 June, 2018"},{"reason_id":"11","reason":"Refuse sample","reason_code":"R","comment":"refuse sample","timestamp":"19 June, 2018"},{"reason_id":"10","reason":"Camp sampling","reason_code":"A","comment":"camp sampling","timestamp":"19 June, 2018"},{"reason_id":"9","reason":"Gift article","reason_code":"G","comment":"gift article","timestamp":"19 June, 2018"},{"reason_id":"8","reason":"Chemist survey","reason_code":"C","comment":"chemist survey","timestamp":"19 June, 2018"},{"reason_id":"7","reason":"Instant personal use","reason_code":"I","comment":"instant personal use","timestamp":"19 June, 2018"},{"reason_id":"6","reason":"Special promotion sample","reason_code":"S","comment":"special promotion sample","timestamp":"19 June, 2018"},{"reason_id":"5","reason":"Personal use","reason_code":"P","comment":"personal use","timestamp":"19 June, 2018"},{"reason_id":"4","reason":"Regular sample","reason_code":"R","comment":"regular sample","timestamp":"19 June, 2018"}]
     */

    private String total;
    private int success;
    private String message;
    private List<ReasonsBean> reasons;

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

    public List<ReasonsBean> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonsBean> reasons) {
        this.reasons = reasons;
    }

    public static class ReasonsBean {
        /**
         * reason_id : 12
         * reason : Test
         * reason_code : TEST
         * comment : test
         * timestamp : 19 June, 2018
         */

        private String reason_id;
        private String reason;
        private String reason_code;
        private String comment;
        private String timestamp;

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getReason_code() {
            return reason_code;
        }

        public void setReason_code(String reason_code) {
            this.reason_code = reason_code;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
