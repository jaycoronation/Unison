package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 19-Jul-18.
 */
public class DashboardResponse
{

    /**
     * report : {"staff_summary":{"name":"Kiran Patel","division":"Sonus","head_quarters":"marketplace","date":"19 July, 2018","work_day":1},"cons":{"total":0,"covered":0,"avg":0},"gp":{"total":2,"covered":0,"avg":0},"imp":{"total":0,"covered":0,"avg":0},"pot":{"total":0,"covered":0,"avg":0},"total":{"total_coverage":2,"call_coverage":0,"call_average":0}}
     * success : 1
     */

    private ReportBean report = new ReportBean();
    private int success = 0;

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

    public static class ReportBean {
        /**
         * staff_summary : {"name":"Kiran Patel","division":"Sonus","head_quarters":"marketplace","date":"19 July, 2018","work_day":1}
         * cons : {"total":0,"covered":0,"avg":0}
         * gp : {"total":2,"covered":0,"avg":0}
         * imp : {"total":0,"covered":0,"avg":0}
         * pot : {"total":0,"covered":0,"avg":0}
         * total : {"total_coverage":2,"call_coverage":0,"call_average":0}
         */

        private StaffSummaryBean staff_summary;
        private ConsBean cons;
        private GpBean gp;
        private ImpBean imp;
        private PotBean pot;
        private TotalBean total;

        public StaffSummaryBean getStaff_summary() {
            return staff_summary;
        }

        public void setStaff_summary(StaffSummaryBean staff_summary) {
            this.staff_summary = staff_summary;
        }

        public ConsBean getCons() {
            return cons;
        }

        public void setCons(ConsBean cons) {
            this.cons = cons;
        }

        public GpBean getGp() {
            return gp;
        }

        public void setGp(GpBean gp) {
            this.gp = gp;
        }

        public ImpBean getImp() {
            return imp;
        }

        public void setImp(ImpBean imp) {
            this.imp = imp;
        }

        public PotBean getPot() {
            return pot;
        }

        public void setPot(PotBean pot) {
            this.pot = pot;
        }

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public static class StaffSummaryBean {
            /**
             * name : Kiran Patel
             * division : Sonus
             * head_quarters : marketplace
             * date : 19 July, 2018
             * work_day : 1
             */

            private String name = "";
            private String division = "";
            private String head_quarters = "";
            private String date = "";
            private int work_day = 0;
            private String total_days = "0";
            private double day_avg = 0;

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

            public String getTotal_days() {
                return total_days;
            }

            public void setTotal_days(String total_days) {
                this.total_days = total_days;
            }

            public double getDay_avg() {
                return day_avg;
            }

            public void setDay_avg(double day_avg) {
                this.day_avg = day_avg;
            }
        }

        public static class ConsBean {
            /**
             * total : 0
             * covered : 0
             * avg : 0
             */

            private int total = 0;
            private int covered = 0;
            private float avg = 0;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getCovered() {
                return covered;
            }

            public void setCovered(int covered) {
                this.covered = covered;
            }

            public float getAvg() {
                return avg;
            }

            public void setAvg(float avg) {
                this.avg = avg;
            }
        }

        public static class GpBean {
            /**
             * total : 2
             * covered : 0
             * avg : 0
             */

            private int total = 0;
            private int covered = 0;
            private float avg = 0;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getCovered() {
                return covered;
            }

            public void setCovered(int covered) {
                this.covered = covered;
            }

            public float getAvg() {
                return avg;
            }

            public void setAvg(float avg) {
                this.avg = avg;
            }
        }

        public static class ImpBean {
            /**
             * total : 0
             * covered : 0
             * avg : 0
             */

            private int total = 0;
            private int covered = 0;
            private float avg = 0;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getCovered() {
                return covered;
            }

            public void setCovered(int covered) {
                this.covered = covered;
            }

            public float getAvg() {
                return avg;
            }

            public void setAvg(float avg) {
                this.avg = avg;
            }
        }

        public static class PotBean {
            /**
             * total : 0
             * covered : 0
             * avg : 0
             */

            private int total = 0;
            private int covered = 0;
            private float avg = 0;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getCovered() {
                return covered;
            }

            public void setCovered(int covered) {
                this.covered = covered;
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
             * total_coverage : 2
             * call_coverage : 0
             * call_average : 0
             */

            private int total_coverage = 0;
            private int call_coverage = 0;
            private float call_average = 0;

            public int getTotal_coverage() {
                return total_coverage;
            }

            public void setTotal_coverage(int total_coverage) {
                this.total_coverage = total_coverage;
            }

            public int getCall_coverage() {
                return call_coverage;
            }

            public void setCall_coverage(int call_coverage) {
                this.call_coverage = call_coverage;
            }

            public float getCall_average() {
                return call_average;
            }

            public void setCall_average(float call_average) {
                this.call_average = call_average;
            }
        }
    }
}
