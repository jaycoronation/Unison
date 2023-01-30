package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 28-Mar-19.
 */
public class SearchDoctorResponse
{

    /**
     * doctors : [{"doctor_id":"30107","doctor":"DR. JAVED A. KADIWAL, BUMS","speciality":"GENERAL PRACTITIONER","area":"CHAPPI","city":"CHAPPI"},{"doctor_id":"23919","doctor":"DR. JAVED AHMED,BHMS","speciality":"GENERAL PRACTITIONER","area":"GOMTIPUR","city":"AHMEDABAD"},{"doctor_id":"20525","doctor":"DR. JAVED BHUVAR,BHMS","speciality":"GENERAL PRACTITIONER","area":"BHAVNAGAR","city":"BHAVNAGAR"},{"doctor_id":"36118","doctor":"DR. JAVED DIWAN","speciality":"GENERAL PRACTITIONER","area":"O P ROAD","city":"BARODA"},{"doctor_id":"12552","doctor":"DR. JAVED G. KANUNGA,DHMS","speciality":"GENERAL PRACTITIONER","area":"SHAHPORE","city":"SURAT"},{"doctor_id":"28320","doctor":"DR. JAVED GUNDAWALA,DHMS","speciality":"GENERAL PRACTITIONER","area":"PORBANDAR","city":"PORBANDAR"},{"doctor_id":"30720","doctor":"DR. JAVED HAKIM,RMP","speciality":"GENERAL PRACTITIONER","area":"SANTRAMPUR","city":"DAHOD"},{"doctor_id":"17029","doctor":"DR. JAVED M. VAKIL,MD,DM","speciality":"NEPHROLOGIST","area":"AHMEDABAD","city":"AHMEDABAD"},{"doctor_id":"30538","doctor":"DR. JAVED SACHORA,MBBS","speciality":"GENERAL PRACTITIONER","area":"JUHAPURA","city":"AHMEDABAD"},{"doctor_id":"11956","doctor":"DR. JAVED SHAIKH,MD","speciality":"PHYSICIAN","area":"MEHSANA","city":"MEHSANA"},{"doctor_id":"37378","doctor":"DR. JAVED VASKA,","speciality":"ORTHOPAEDIC","area":"JAMBUSAR","city":"JAMBUSAR"}]
     * success : 1
     * message : 11 doctors found!
     */

    private int success = 0;
    private String message = "";
    private List<DoctorsBean> doctors = new ArrayList<>();

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

    public List<DoctorsBean> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorsBean> doctors) {
        this.doctors = doctors;
    }

    public static class DoctorsBean {
        /**
         * doctor_id : 30107
         * doctor : DR. JAVED A. KADIWAL, BUMS
         * speciality : GENERAL PRACTITIONER
         * area : CHAPPI
         * city : CHAPPI
         */

        private String doctor_id = "";
        private String doctor = "";
        private String speciality = "";
        private String area = "";
        private String city = "";

        public String getDoctor_id() {
            return doctor_id;
        }

        public void setDoctor_id(String doctor_id) {
            this.doctor_id = doctor_id;
        }

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
