package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

public class DBReason extends SugarRecord<DBReason> {
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

    public DBReason(String reason_id, String reason, String reason_code, String comment, String timestamp) {
        this.reason_id = reason_id;
        this.reason = reason;
        this.reason_code = reason_code;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public DBReason() {
    }

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