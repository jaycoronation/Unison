package com.unisonpharmaceuticals.model.for_sugar;

import com.unisonpharmaceuticals.sugar.com.orm.SugarRecord;

public class DBSpeciality extends SugarRecord<DBSpeciality> {
        /**
         * speciality : Urologist
         * speciality_id : 32
         * speciality_code : 0
         */

        private String speciality = "";
        private String speciality_id = "";
        private String speciality_code = "";

    public DBSpeciality(String speciality, String speciality_id, String speciality_code)
    {
        this.speciality = speciality;
        this.speciality_id = speciality_id;
        this.speciality_code = speciality_code;
    }

    public DBSpeciality(){}

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getSpeciality_id() {
            return speciality_id;
        }

        public void setSpeciality_id(String speciality_id) {
            this.speciality_id = speciality_id;
        }

        public String getSpeciality_code() {
            return speciality_code;
        }

        public void setSpeciality_code(String speciality_code) {
            this.speciality_code = speciality_code;
        }
    }