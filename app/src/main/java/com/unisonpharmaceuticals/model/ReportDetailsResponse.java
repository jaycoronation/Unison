package com.unisonpharmaceuticals.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tushar Vataliya on 09-Jul-18.
 */
public class ReportDetailsResponse
{

    /**
     * report : {"staff_summary":{"name":"Mayur Chatbar","division":"Rhythm","head_quarters":"BhavnagarP3","date":"06 July, 2018","work_day":2},"doctor_summary":{"overall":{"cons":"7","gp":"3","ap":"0","resi":"0","total":"10","avg":"0.00","imp":"0","potential":"0","nc_total":"0","nc_gp":"0","nc_cons":"0","nc_imp":"0","tcall":"0","zcr_call":"0","int":"0"},"previous":{"cons":4,"gp":0,"ap":0,"resi":1,"total":5,"avg":5,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"today":{"cons":1,"gp":1,"ap":0,"resi":1,"total":3,"avg":3,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"total":{"cons":5,"gp":1,"ap":0,"resi":2,"total":8,"avg":4,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"avg":{"cons":71.43,"gp":33.33,"ap":0,"resi":0,"total":80,"avg":0,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}}}
     * success : 1
     * message : Records found
     */

    private ReportBean report = new ReportBean();
    private int success;
    private String message;

    public ReportBean getReport() {
        return report;
    }

    public void setReport(ReportBean report) {
        this.report = report;
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

    public static class ReportBean {
        /**
         * staff_summary : {"name":"Mayur Chatbar","division":"Rhythm","head_quarters":"BhavnagarP3","date":"06 July, 2018","work_day":2}
         * doctor_summary : {"overall":{"cons":"7","gp":"3","ap":"0","resi":"0","total":"10","avg":"0.00","imp":"0","potential":"0","nc_total":"0","nc_gp":"0","nc_cons":"0","nc_imp":"0","tcall":"0","zcr_call":"0","int":"0"},"previous":{"cons":4,"gp":0,"ap":0,"resi":1,"total":5,"avg":5,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"today":{"cons":1,"gp":1,"ap":0,"resi":1,"total":3,"avg":3,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"total":{"cons":5,"gp":1,"ap":0,"resi":2,"total":8,"avg":4,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0},"avg":{"cons":71.43,"gp":33.33,"ap":0,"resi":0,"total":80,"avg":0,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}}
         */

        private StaffSummaryBean staff_summary;
        private DoctorSummaryBean doctor_summary;

        public StaffSummaryBean getStaff_summary() {
            return staff_summary;
        }

        public void setStaff_summary(StaffSummaryBean staff_summary) {
            this.staff_summary = staff_summary;
        }

        public DoctorSummaryBean getDoctor_summary() {
            return doctor_summary;
        }

        public void setDoctor_summary(DoctorSummaryBean doctor_summary) {
            this.doctor_summary = doctor_summary;
        }

        public static class StaffSummaryBean {
            /**
             * name : Mayur Chatbar
             * division : Rhythm
             * head_quarters : BhavnagarP3
             * date : 06 July, 2018
             * work_day : 2
             */

            private String name = "";;
            private String division = "";;
            private String head_quarters = "";;
            private String date = "";;
            private String work_area = "";
            private int work_day = 0;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDivision() {
                return division;
            }

            public void setDivision(String division) {
                this.division = division;
            }

            public String getHead_quarters() {
                return head_quarters;
            }

            public void setHead_quarters(String head_quarters) {
                this.head_quarters = head_quarters;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getWork_day() {
                return work_day;
            }

            public void setWork_day(int work_day) {
                this.work_day = work_day;
            }

            public String getWork_area() {
                return work_area;
            }

            public void setWork_area(String work_area) {
                this.work_area = work_area;
            }
        }

        public static class DoctorSummaryBean {
            /**
             * overall : {"cons":"7","gp":"3","ap":"0","resi":"0","total":"10","avg":"0.00","imp":"0","potential":"0","nc_total":"0","nc_gp":"0","nc_cons":"0","nc_imp":"0","tcall":"0","zcr_call":"0","int":"0"}
             * previous : {"cons":4,"gp":0,"ap":0,"resi":1,"total":5,"avg":5,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}
             * today : {"cons":1,"gp":1,"ap":0,"resi":1,"total":3,"avg":3,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}
             * total : {"cons":5,"gp":1,"ap":0,"resi":2,"total":8,"avg":4,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}
             * avg : {"cons":71.43,"gp":33.33,"ap":0,"resi":0,"total":80,"avg":0,"imp":0,"potential":0,"nc_total":0,"nc_gp":0,"nc_cons":0,"nc_imp":0,"tcall":0,"zcr_call":0,"int":0}
             */

            private OverallBean overall = new OverallBean();
            private PreviousBean previous = new PreviousBean();
            private TodayBean today = new TodayBean();
            private TotalBean total = new TotalBean();
            private AvgBean avg = new AvgBean();

            public OverallBean getOverall() {
                return overall;
            }

            public void setOverall(OverallBean overall) {
                this.overall = overall;
            }

            public PreviousBean getPrevious() {
                return previous;
            }

            public void setPrevious(PreviousBean previous) {
                this.previous = previous;
            }

            public TodayBean getToday() {
                return today;
            }

            public void setToday(TodayBean today) {
                this.today = today;
            }

            public TotalBean getTotal() {
                return total;
            }

            public void setTotal(TotalBean total) {
                this.total = total;
            }

            public AvgBean getAvg() {
                return avg;
            }

            public void setAvg(AvgBean avg) {
                this.avg = avg;
            }

            public static class OverallBean {
                /**
                 * cons : 7
                 * gp : 3
                 * ap : 0
                 * resi : 0
                 * total : 10
                 * avg : 0.00
                 * imp : 0
                 * potential : 0
                 * nc_total : 0
                 * nc_gp : 0
                 * nc_cons : 0
                 * nc_imp : 0
                 * tcall : 0
                 * zcr_call : 0
                 * int : 0
                 */

                private int cons;
                private int gp;
                private int ap;
                private int resi;
                private int total;
                private int avg;
                private int imp;
                private int potential;
                private int nc_total;
                private int nc_gp;
                private int nc_cons;
                private int nc_imp;
                private int tcall;
                private int zcr_call;
                @SerializedName("int")
                private int intX;

                public int getCons() {
                    return cons;
                }

                public void setCons(int cons) {
                    this.cons = cons;
                }

                public int getGp() {
                    return gp;
                }

                public void setGp(int gp) {
                    this.gp = gp;
                }

                public int getAp() {
                    return ap;
                }

                public void setAp(int ap) {
                    this.ap = ap;
                }

                public int getResi() {
                    return resi;
                }

                public void setResi(int resi) {
                    this.resi = resi;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public int getAvg() {
                    return avg;
                }

                public void setAvg(int avg) {
                    this.avg = avg;
                }

                public int getImp() {
                    return imp;
                }

                public void setImp(int imp) {
                    this.imp = imp;
                }

                public int getPotential() {
                    return potential;
                }

                public void setPotential(int potential) {
                    this.potential = potential;
                }

                public int getNc_total() {
                    return nc_total;
                }

                public void setNc_total(int nc_total) {
                    this.nc_total = nc_total;
                }

                public int getNc_gp() {
                    return nc_gp;
                }

                public void setNc_gp(int nc_gp) {
                    this.nc_gp = nc_gp;
                }

                public int getNc_cons() {
                    return nc_cons;
                }

                public void setNc_cons(int nc_cons) {
                    this.nc_cons = nc_cons;
                }

                public int getNc_imp() {
                    return nc_imp;
                }

                public void setNc_imp(int nc_imp) {
                    this.nc_imp = nc_imp;
                }

                public int getTcall() {
                    return tcall;
                }

                public void setTcall(int tcall) {
                    this.tcall = tcall;
                }

                public int getZcr_call() {
                    return zcr_call;
                }

                public void setZcr_call(int zcr_call) {
                    this.zcr_call = zcr_call;
                }

                public int getIntX() {
                    return intX;
                }

                public void setIntX(int intX) {
                    this.intX = intX;
                }
            }

            public static class PreviousBean {

                /**
                 * cons : 84
                 * repeat_cons : 0
                 * gp : 78
                 * repeat_gp : 0
                 * ap : 0
                 * repeat_ap : 0
                 * resi : 0
                 * repeat_resi : 0
                 * total : 162
                 * repeat_total : 0
                 * imp : 87
                 * repeat_imp : 0
                 * potential : 10
                 * repeat_pot : 0
                 * new : 0
                 * nc_total : 0
                 * nc_gp : 0
                 * nc_cons : 0
                 * nc_imp : 0
                 * tcall : 0
                 * zcr_call : 0
                 * int : 0
                 * avg : 13.5
                 */

                private int cons;
                private int repeat_cons;
                private int gp;
                private int repeat_gp;
                private int ap;
                private int repeat_ap;
                private int resi;
                private int repeat_resi;
                private int total;
                private int repeat_total;
                private int imp;
                private int repeat_imp;
                private int potential;
                private int repeat_pot;
                @SerializedName("new")
                private int newX;
                private int nc_total;
                private int nc_gp;
                private int nc_cons;
                private int nc_imp;
                private int tcall;
                private int zcr_call;
                @SerializedName("int")
                private int intX;
                private float avg;

                public int getCons() {
                    return cons;
                }

                public void setCons(int cons) {
                    this.cons = cons;
                }

                public int getRepeat_cons() {
                    return repeat_cons;
                }

                public void setRepeat_cons(int repeat_cons) {
                    this.repeat_cons = repeat_cons;
                }

                public int getGp() {
                    return gp;
                }

                public void setGp(int gp) {
                    this.gp = gp;
                }

                public int getRepeat_gp() {
                    return repeat_gp;
                }

                public void setRepeat_gp(int repeat_gp) {
                    this.repeat_gp = repeat_gp;
                }

                public int getAp() {
                    return ap;
                }

                public void setAp(int ap) {
                    this.ap = ap;
                }

                public int getRepeat_ap() {
                    return repeat_ap;
                }

                public void setRepeat_ap(int repeat_ap) {
                    this.repeat_ap = repeat_ap;
                }

                public int getResi() {
                    return resi;
                }

                public void setResi(int resi) {
                    this.resi = resi;
                }

                public int getRepeat_resi() {
                    return repeat_resi;
                }

                public void setRepeat_resi(int repeat_resi) {
                    this.repeat_resi = repeat_resi;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public int getRepeat_total() {
                    return repeat_total;
                }

                public void setRepeat_total(int repeat_total) {
                    this.repeat_total = repeat_total;
                }

                public int getImp() {
                    return imp;
                }

                public void setImp(int imp) {
                    this.imp = imp;
                }

                public int getRepeat_imp() {
                    return repeat_imp;
                }

                public void setRepeat_imp(int repeat_imp) {
                    this.repeat_imp = repeat_imp;
                }

                public int getPotential() {
                    return potential;
                }

                public void setPotential(int potential) {
                    this.potential = potential;
                }

                public int getRepeat_pot() {
                    return repeat_pot;
                }

                public void setRepeat_pot(int repeat_pot) {
                    this.repeat_pot = repeat_pot;
                }

                public int getNewX() {
                    return newX;
                }

                public void setNewX(int newX) {
                    this.newX = newX;
                }

                public int getNc_total() {
                    return nc_total;
                }

                public void setNc_total(int nc_total) {
                    this.nc_total = nc_total;
                }

                public int getNc_gp() {
                    return nc_gp;
                }

                public void setNc_gp(int nc_gp) {
                    this.nc_gp = nc_gp;
                }

                public int getNc_cons() {
                    return nc_cons;
                }

                public void setNc_cons(int nc_cons) {
                    this.nc_cons = nc_cons;
                }

                public int getNc_imp() {
                    return nc_imp;
                }

                public void setNc_imp(int nc_imp) {
                    this.nc_imp = nc_imp;
                }

                public int getTcall() {
                    return tcall;
                }

                public void setTcall(int tcall) {
                    this.tcall = tcall;
                }

                public int getZcr_call() {
                    return zcr_call;
                }

                public void setZcr_call(int zcr_call) {
                    this.zcr_call = zcr_call;
                }

                public int getIntX() {
                    return intX;
                }

                public void setIntX(int intX) {
                    this.intX = intX;
                }

                public float getAvg() {
                    return avg;
                }

                public void setAvg(float avg) {
                    this.avg = avg;
                }
            }

            public static class TodayBean {
                /**
                 * cons : 1
                 * gp : 1
                 * ap : 0
                 * resi : 1
                 * total : 3
                 * avg : 3
                 * imp : 0
                 * potential : 0
                 * nc_total : 0
                 * nc_gp : 0
                 * nc_cons : 0
                 * nc_imp : 0
                 * tcall : 0
                 * zcr_call : 0
                 * int : 0
                 */

                private int cons;
                private int repeat_cons;
                private int gp;
                private int repeat_gp;
                private int ap;
                private int repeat_ap;
                private int resi;
                private int repeat_resi;
                private int total;
                private int repeat_total;
                private int imp;
                private int repeat_imp;
                private int potential;
                private int repeat_pot;
                @SerializedName("new")
                private int newX;
                private int nc_total;
                private int nc_gp;
                private int nc_cons;
                private int nc_imp;
                private int tcall;
                private int zcr_call;
                @SerializedName("int")
                private int intX;
                private float avg;

                public int getCons() {
                    return cons;
                }

                public void setCons(int cons) {
                    this.cons = cons;
                }

                public int getRepeat_cons() {
                    return repeat_cons;
                }

                public void setRepeat_cons(int repeat_cons) {
                    this.repeat_cons = repeat_cons;
                }

                public int getGp() {
                    return gp;
                }

                public void setGp(int gp) {
                    this.gp = gp;
                }

                public int getRepeat_gp() {
                    return repeat_gp;
                }

                public void setRepeat_gp(int repeat_gp) {
                    this.repeat_gp = repeat_gp;
                }

                public int getAp() {
                    return ap;
                }

                public void setAp(int ap) {
                    this.ap = ap;
                }

                public int getRepeat_ap() {
                    return repeat_ap;
                }

                public void setRepeat_ap(int repeat_ap) {
                    this.repeat_ap = repeat_ap;
                }

                public int getResi() {
                    return resi;
                }

                public void setResi(int resi) {
                    this.resi = resi;
                }

                public int getRepeat_resi() {
                    return repeat_resi;
                }

                public void setRepeat_resi(int repeat_resi) {
                    this.repeat_resi = repeat_resi;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public int getRepeat_total() {
                    return repeat_total;
                }

                public void setRepeat_total(int repeat_total) {
                    this.repeat_total = repeat_total;
                }

                public int getImp() {
                    return imp;
                }

                public void setImp(int imp) {
                    this.imp = imp;
                }

                public int getRepeat_imp() {
                    return repeat_imp;
                }

                public void setRepeat_imp(int repeat_imp) {
                    this.repeat_imp = repeat_imp;
                }

                public int getPotential() {
                    return potential;
                }

                public void setPotential(int potential) {
                    this.potential = potential;
                }

                public int getRepeat_pot() {
                    return repeat_pot;
                }

                public void setRepeat_pot(int repeat_pot) {
                    this.repeat_pot = repeat_pot;
                }

                public int getNewX() {
                    return newX;
                }

                public void setNewX(int newX) {
                    this.newX = newX;
                }

                public int getNc_total() {
                    return nc_total;
                }

                public void setNc_total(int nc_total) {
                    this.nc_total = nc_total;
                }

                public int getNc_gp() {
                    return nc_gp;
                }

                public void setNc_gp(int nc_gp) {
                    this.nc_gp = nc_gp;
                }

                public int getNc_cons() {
                    return nc_cons;
                }

                public void setNc_cons(int nc_cons) {
                    this.nc_cons = nc_cons;
                }

                public int getNc_imp() {
                    return nc_imp;
                }

                public void setNc_imp(int nc_imp) {
                    this.nc_imp = nc_imp;
                }

                public int getTcall() {
                    return tcall;
                }

                public void setTcall(int tcall) {
                    this.tcall = tcall;
                }

                public int getZcr_call() {
                    return zcr_call;
                }

                public void setZcr_call(int zcr_call) {
                    this.zcr_call = zcr_call;
                }

                public int getIntX() {
                    return intX;
                }

                public void setIntX(int intX) {
                    this.intX = intX;
                }

                public float getAvg() {
                    return avg;
                }

                public void setAvg(float avg) {
                    this.avg = avg;
                }
            }

            public static class TotalBean {
                /**
                 * cons : 5
                 * gp : 1
                 * ap : 0
                 * resi : 2
                 * total : 8
                 * avg : 4
                 * imp : 0
                 * potential : 0
                 * nc_total : 0
                 * nc_gp : 0
                 * nc_cons : 0
                 * nc_imp : 0
                 * tcall : 0
                 * zcr_call : 0
                 * int : 0
                 */

                private int cons;
                private int repeat_cons;
                private int gp;
                private int repeat_gp;
                private int ap;
                private int repeat_ap;
                private int resi;
                private int repeat_resi;
                private int total;
                private int repeat_total;
                private int imp;
                private int repeat_imp;
                private int potential;
                private int repeat_pot;
                @SerializedName("new")
                private int newX;
                private int nc_total;
                private int nc_gp;
                private int nc_cons;
                private int nc_imp;
                private int tcall;
                private int zcr_call;
                @SerializedName("int")
                private int intX;
                private float avg;

                public int getCons() {
                    return cons;
                }

                public void setCons(int cons) {
                    this.cons = cons;
                }

                public int getRepeat_cons() {
                    return repeat_cons;
                }

                public void setRepeat_cons(int repeat_cons) {
                    this.repeat_cons = repeat_cons;
                }

                public int getGp() {
                    return gp;
                }

                public void setGp(int gp) {
                    this.gp = gp;
                }

                public int getRepeat_gp() {
                    return repeat_gp;
                }

                public void setRepeat_gp(int repeat_gp) {
                    this.repeat_gp = repeat_gp;
                }

                public int getAp() {
                    return ap;
                }

                public void setAp(int ap) {
                    this.ap = ap;
                }

                public int getRepeat_ap() {
                    return repeat_ap;
                }

                public void setRepeat_ap(int repeat_ap) {
                    this.repeat_ap = repeat_ap;
                }

                public int getResi() {
                    return resi;
                }

                public void setResi(int resi) {
                    this.resi = resi;
                }

                public int getRepeat_resi() {
                    return repeat_resi;
                }

                public void setRepeat_resi(int repeat_resi) {
                    this.repeat_resi = repeat_resi;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public int getRepeat_total() {
                    return repeat_total;
                }

                public void setRepeat_total(int repeat_total) {
                    this.repeat_total = repeat_total;
                }

                public int getImp() {
                    return imp;
                }

                public void setImp(int imp) {
                    this.imp = imp;
                }

                public int getRepeat_imp() {
                    return repeat_imp;
                }

                public void setRepeat_imp(int repeat_imp) {
                    this.repeat_imp = repeat_imp;
                }

                public int getPotential() {
                    return potential;
                }

                public void setPotential(int potential) {
                    this.potential = potential;
                }

                public int getRepeat_pot() {
                    return repeat_pot;
                }

                public void setRepeat_pot(int repeat_pot) {
                    this.repeat_pot = repeat_pot;
                }

                public int getNewX() {
                    return newX;
                }

                public void setNewX(int newX) {
                    this.newX = newX;
                }

                public int getNc_total() {
                    return nc_total;
                }

                public void setNc_total(int nc_total) {
                    this.nc_total = nc_total;
                }

                public int getNc_gp() {
                    return nc_gp;
                }

                public void setNc_gp(int nc_gp) {
                    this.nc_gp = nc_gp;
                }

                public int getNc_cons() {
                    return nc_cons;
                }

                public void setNc_cons(int nc_cons) {
                    this.nc_cons = nc_cons;
                }

                public int getNc_imp() {
                    return nc_imp;
                }

                public void setNc_imp(int nc_imp) {
                    this.nc_imp = nc_imp;
                }

                public int getTcall() {
                    return tcall;
                }

                public void setTcall(int tcall) {
                    this.tcall = tcall;
                }

                public int getZcr_call() {
                    return zcr_call;
                }

                public void setZcr_call(int zcr_call) {
                    this.zcr_call = zcr_call;
                }

                public int getIntX() {
                    return intX;
                }

                public void setIntX(int intX) {
                    this.intX = intX;
                }

                public float getAvg() {
                    return avg;
                }

                public void setAvg(float avg) {
                    this.avg = avg;
                }
            }

            public static class AvgBean {
                /**
                 * cons : 71.43
                 * gp : 33.33
                 * ap : 0
                 * resi : 0
                 * total : 80
                 * avg : 0
                 * imp : 0
                 * potential : 0
                 * nc_total : 0
                 * nc_gp : 0
                 * nc_cons : 0
                 * nc_imp : 0
                 * tcall : 0
                 * zcr_call : 0
                 * int : 0
                 */

                private double cons;
                private double gp;
                private double ap;
                private double resi;
                private double total;
                private double avg;
                private double imp;
                private double potential;
                private double nc_total;
                private double nc_gp;
                private double nc_cons;
                private double nc_imp;
                private double tcall;
                private double zcr_call;
                @SerializedName("int")
                private int intX;

                public double getCons() {
                    return cons;
                }

                public void setCons(double cons) {
                    this.cons = cons;
                }

                public double getGp() {
                    return gp;
                }

                public void setGp(double gp) {
                    this.gp = gp;
                }

                public double getAp() {
                    return ap;
                }

                public void setAp(int ap) {
                    this.ap = ap;
                }

                public double getResi() {
                    return resi;
                }

                public void setResi(int resi) {
                    this.resi = resi;
                }

                public double getTotal() {
                    return total;
                }

                public void setTotal(float total) {
                    this.total = total;
                }

                public double getAvg() {
                    return avg;
                }

                public void setAvg(float avg) {
                    this.avg = avg;
                }

                public double getImp() {
                    return imp;
                }

                public void setImp(int imp) {
                    this.imp = imp;
                }

                public double getPotential() {
                    return potential;
                }

                public void setPotential(int potential) {
                    this.potential = potential;
                }

                public double getNc_total() {
                    return nc_total;
                }

                public void setNc_total(int nc_total) {
                    this.nc_total = nc_total;
                }

                public double getNc_gp() {
                    return nc_gp;
                }

                public void setNc_gp(int nc_gp) {
                    this.nc_gp = nc_gp;
                }

                public double getNc_cons() {
                    return nc_cons;
                }

                public void setNc_cons(int nc_cons) {
                    this.nc_cons = nc_cons;
                }

                public double getNc_imp() {
                    return nc_imp;
                }

                public void setNc_imp(int nc_imp) {
                    this.nc_imp = nc_imp;
                }

                public double getTcall() {
                    return tcall;
                }

                public void setTcall(int tcall) {
                    this.tcall = tcall;
                }

                public double getZcr_call() {
                    return zcr_call;
                }

                public void setZcr_call(int zcr_call) {
                    this.zcr_call = zcr_call;
                }

                public double getIntX() {
                    return intX;
                }

                public void setIntX(int intX) {
                    this.intX = intX;
                }
            }
        }
    }
}
