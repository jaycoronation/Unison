package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tushar Vataliya on 16-Jul-18.
 */
public class SubmittedResponse
{

    /**
     * report : {"staff_summary":{"name":"Kiran Patel","division":"Sonus","head_quarters":"marketplace","date":"19 July, 2018","work_day":5,"work_with":"SELF","work_area":"V. s. hospital [Ahmedabad]"},"data":[{"doctor_name":"Semil Shah","degree":"PSY","category":"","report_type":"DCR","no_of_internee":"","is_new_cycle":1,"report_id":"49","last_visit":"10 July, 2018","mon_business":0,"products":[{"item_id":"ACR","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100},{"item_id":"AER","quantity":"5","reason_code":"P","total":0,"balance":0,"percentage":100},{"item_id":"AK1","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100}],"mec":"M-N","ww":"M","total":5,"focus_products":[{"product_id":"14","focus_item_code":"ALL","focus_item_id_code":"ALL","focus_generic":"ALLOUT (PHARMA)","focus_type":"New"},{"product_id":"16","focus_item_code":"FPPN002","focus_item_id_code":"AER","focus_generic":"P.S Naso","focus_type":"Enhance"}]}],"remark":[{"remark":"test remark","remark_id":"7"}]}
     * success : 1
     * message : Records found
     */

    private ReportBean report;
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
         * staff_summary : {"name":"Kiran Patel","division":"Sonus","head_quarters":"marketplace","date":"19 July, 2018","work_day":5,"work_with":"SELF","work_area":"V. s. hospital [Ahmedabad]"}
         * data : [{"doctor_name":"Semil Shah","degree":"PSY","category":"","report_type":"DCR","no_of_internee":"","is_new_cycle":1,"report_id":"49","last_visit":"10 July, 2018","mon_business":0,"products":[{"item_id":"ACR","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100},{"item_id":"AER","quantity":"5","reason_code":"P","total":0,"balance":0,"percentage":100},{"item_id":"AK1","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100}],"mec":"M-N","ww":"M","total":5,"focus_products":[{"product_id":"14","focus_item_code":"ALL","focus_item_id_code":"ALL","focus_generic":"ALLOUT (PHARMA)","focus_type":"New"},{"product_id":"16","focus_item_code":"FPPN002","focus_item_id_code":"AER","focus_generic":"P.S Naso","focus_type":"Enhance"}]}]
         * remark : [{"remark":"test remark","remark_id":"7"}]
         */

        private StaffSummaryBean staff_summary;
        private List<DataBean> data = new ArrayList<>();
        private List<RemarkBean> remark;
        private List<AdviceBean> advice;

        public StaffSummaryBean getStaff_summary() {
            return staff_summary;
        }

        public void setStaff_summary(StaffSummaryBean staff_summary) {
            this.staff_summary = staff_summary;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<RemarkBean> getRemark() {
            return remark;
        }

        public void setRemark(List<RemarkBean> remark) {
            this.remark = remark;
        }

        public List<AdviceBean> getAdvice() {
            return advice;
        }

        public void setAdvice(List<AdviceBean> advice) {
            this.advice = advice;
        }

        public static class StaffSummaryBean {
            /**
             * name : Kiran Patel
             * division : Sonus
             * head_quarters : marketplace
             * date : 19 July, 2018
             * work_day : 5
             * work_with : SELF
             * work_area : V. s. hospital [Ahmedabad]
             */

            private String name;
            private String division;
            private String head_quarters;
            private String date;
            private int work_day;
            private String work_with;
            private String work_area;

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

            public String getWork_with() {
                return work_with;
            }

            public void setWork_with(String work_with) {
                this.work_with = work_with;
            }

            public String getWork_area() {
                return work_area;
            }

            public void setWork_area(String work_area) {
                this.work_area = work_area;
            }
        }

        public static class DataBean {
            /**
             * doctor_name : Semil Shah
             * degree : PSY
             * category :
             * report_type : DCR
             * no_of_internee :
             * is_new_cycle : 1
             * report_id : 49
             * last_visit : 10 July, 2018
             * mon_business : 0
             * products : [{"item_id":"ACR","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100},{"item_id":"AER","quantity":"5","reason_code":"P","total":0,"balance":0,"percentage":100},{"item_id":"AK1","quantity":"","reason_code":"R","total":0,"balance":0,"percentage":100}]
             * mec : M-N
             * ww : M
             * total : 5
             * focus_products : [{"product_id":"14","focus_item_code":"ALL","focus_item_id_code":"ALL","focus_generic":"ALLOUT (PHARMA)","focus_type":"New"},{"product_id":"16","focus_item_code":"FPPN002","focus_item_id_code":"AER","focus_generic":"P.S Naso","focus_type":"Enhance"}]
             */

            private String doctor_name = "";
            private String doctor_id = "";
            private String degree = "";
            private String category = "";
            private String report_type = "";
            private String no_of_internee = "";
            private int is_new_cycle = 0;
            private String report_id = "";
            private String last_visit = "";
            private int mon_business = 0;
            private String mec = "";
            private String ww = "";
            private String status = "";
            private int total = 0;
            private List<ProductsBean> products = new ArrayList<>();
            private List<FocusProductsBean> focus_products = new ArrayList<>();

            public String getDoctor_name() {
                return doctor_name;
            }

            public void setDoctor_name(String doctor_name) {
                this.doctor_name = doctor_name;
            }

            public String getDegree() {
                return degree;
            }

            public void setDegree(String degree) {
                this.degree = degree;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getReport_type() {
                return report_type;
            }

            public void setReport_type(String report_type) {
                this.report_type = report_type;
            }

            public String getNo_of_internee() {
                return no_of_internee;
            }

            public void setNo_of_internee(String no_of_internee) {
                this.no_of_internee = no_of_internee;
            }

            public int getIs_new_cycle() {
                return is_new_cycle;
            }

            public void setIs_new_cycle(int is_new_cycle) {
                this.is_new_cycle = is_new_cycle;
            }

            public String getReport_id() {
                return report_id;
            }

            public void setReport_id(String report_id) {
                this.report_id = report_id;
            }

            public String getLast_visit() {
                return last_visit;
            }

            public void setLast_visit(String last_visit) {
                this.last_visit = last_visit;
            }

            public int getMon_business() {
                return mon_business;
            }

            public void setMon_business(int mon_business) {
                this.mon_business = mon_business;
            }

            public String getMec() {
                return mec;
            }

            public void setMec(String mec) {
                this.mec = mec;
            }

            public String getWw() {
                return ww;
            }

            public void setWw(String ww) {
                this.ww = ww;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<ProductsBean> getProducts() {
                return products;
            }

            public void setProducts(List<ProductsBean> products) {
                this.products = products;
            }

            public List<FocusProductsBean> getFocus_products() {
                return focus_products;
            }

            public void setFocus_products(List<FocusProductsBean> focus_products) {
                this.focus_products = focus_products;
            }

            public String getDoctor_id() {
                return doctor_id;
            }

            public void setDoctor_id(String doctor_id) {
                this.doctor_id = doctor_id;
            }

            public static class ProductsBean {
                /**
                 * item_id : ACR
                 * quantity :
                 * reason_code : R
                 * total : 0
                 * balance : 0
                 * percentage : 100
                 */

                private String item_id;
                private String quantity;
                private String reason_code;
                private int total;
                private int balance;
                private int percentage;

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getReason_code() {
                    return reason_code;
                }

                public void setReason_code(String reason_code) {
                    this.reason_code = reason_code;
                }

                public int getTotal() {
                    return total;
                }

                public void setTotal(int total) {
                    this.total = total;
                }

                public int getBalance() {
                    return balance;
                }

                public void setBalance(int balance) {
                    this.balance = balance;
                }

                public int getPercentage() {
                    return percentage;
                }

                public void setPercentage(int percentage) {
                    this.percentage = percentage;
                }
            }

            public static class FocusProductsBean {
                /**
                 * product_id : 14
                 * focus_item_code : ALL
                 * focus_item_id_code : ALL
                 * focus_generic : ALLOUT (PHARMA)
                 * focus_type : New
                 */

                private String product_id;
                private String focus_item_code;
                private String focus_item_id_code;
                private String focus_generic;
                private String focus_type;

                public String getProduct_id() {
                    return product_id;
                }

                public void setProduct_id(String product_id) {
                    this.product_id = product_id;
                }

                public String getFocus_item_code() {
                    return focus_item_code;
                }

                public void setFocus_item_code(String focus_item_code) {
                    this.focus_item_code = focus_item_code;
                }

                public String getFocus_item_id_code() {
                    return focus_item_id_code;
                }

                public void setFocus_item_id_code(String focus_item_id_code) {
                    this.focus_item_id_code = focus_item_id_code;
                }

                public String getFocus_generic() {
                    return focus_generic;
                }

                public void setFocus_generic(String focus_generic) {
                    this.focus_generic = focus_generic;
                }

                public String getFocus_type() {
                    return focus_type;
                }

                public void setFocus_type(String focus_type) {
                    this.focus_type = focus_type;
                }
            }
        }

        public static class RemarkBean {
            /**
             * remark : test remark
             * remark_id : 7
             */

            private String remark;
            private String remark_id;

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getRemark_id() {
                return remark_id;
            }

            public void setRemark_id(String remark_id) {
                this.remark_id = remark_id;
            }
        }

        public static class AdviceBean {
            /**
             * remark : test remark
             * remark_id : 7
             */

            private String remark;
            private String remark_id;

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getRemark_id() {
                return remark_id;
            }

            public void setRemark_id(String remark_id) {
                this.remark_id = remark_id;
            }
        }
    }
}
