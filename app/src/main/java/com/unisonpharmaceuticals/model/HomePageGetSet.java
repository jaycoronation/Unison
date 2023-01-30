package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 21-Jun-18.
 */
public class HomePageGetSet
{

    /**
     * success : 1
     * message :
     * banner : [{"image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/banner_.jpg","text":[{"line":""},{"line":""}]},{"image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/banner_men.jpg","text":[{"line":""},{"line":""}]},{"image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/banner_saree.jpg","text":[{"line":""},{"line":""}]},{"image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/chaniya-choli.jpg","text":[{"line":""},{"line":""}]},{"image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/gown_collection.jpg","text":[{"line":""},{"line":""}]}]
     * categories : [{"id":"1","name":"Bridal Collection","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/bridal_mobile.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496647971_bridals.jpg"},{"id":"2","name":"Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/Saree.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496648789_saree.jpg"},{"id":"3","name":"Dresses","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/DRESSES.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496648816_dresses.jpg"},{"id":"4","name":"Chaniya Choli","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/Chaniyacholi.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496648861_chaniya_choli.jpg"},{"id":"5","name":"Gown","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/Gown.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496648889_gowns.jpg"},{"id":"23","name":"Men","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/Mens.jpg","banner_image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1504188829_Groom-Jacket-Collection-2012-02.jpg"}]
     * latestProducts : [{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3895","name":"Green Banarasi Woven Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473552_SR-SHA06-0074.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3894","name":"Dark Blue Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473571_SR-SHA06-0064.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3893","name":"White Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473436_SR-SHA06-0063.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3892","name":"Orange Banarasi Woven Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473391_SR-SHA06-0075.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3891","name":"Light Pink Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473268_SR-SHA06-0062.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3826","name":"Printed Jodhpuri with orange combination","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529323041_51_-_Copy.jpg","price":{"min_mrp_price":13995,"max_mrp_price":13995,"min_price":13995,"max_price":13995}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3825","name":"Printed Jodhpuri with white combination","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529322899_49.jpg","price":{"min_mrp_price":13995,"max_mrp_price":13995,"min_price":13995,"max_price":13995}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3824","name":"Rama Green  With Golden Indowestern","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529322778_47.jpg","price":{"min_mrp_price":10995,"max_mrp_price":10995,"min_price":10995,"max_price":10995}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3823","name":"Pink with Golden Indowestern","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529322669_46.jpg","price":{"min_mrp_price":10595,"max_mrp_price":10595,"min_price":10595,"max_price":10595}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3822","name":"Maroon velvet with heavy work Shwewani","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529322519_41.jpg","price":{"min_mrp_price":16995,"max_mrp_price":16995,"min_price":16995,"max_price":16995}}]
     * saleProducts : [{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3891","name":"Light Pink Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473268_SR-SHA06-0062.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3892","name":"Orange Banarasi Woven Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473391_SR-SHA06-0075.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3893","name":"White Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473436_SR-SHA06-0063.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3894","name":"Dark Blue Woven Print Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473571_SR-SHA06-0064.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}},{"is_in_wishlist":0,"is_out_of_stock":0,"id":"3895","name":"Green Banarasi Woven Saree","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473552_SR-SHA06-0074.jpg","price":{"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}}]
     * testimonials : [{"name":"Sunny Shah","comment":"Fast as usual. Thanks.","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/uploads/testimonial/1496906690_Sunny_Shah.jpg"},{"name":"Ankit Chhelavda","comment":"Fast delivery. Product as described","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/uploads/testimonial/1496907020_Ankit_Chhelavda.jpg"},{"name":"Pruthvirajsinh Gohil","comment":"Superb service I had. very helpful staffs. Very fast delivery","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/uploads/testimonial/1496907052_Pruthviraj_Gohil.jpg"},{"name":"Anisha","comment":"Excellent service next day delivery after ordering","image":"http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/uploads/testimonial/1496907079_Anisha_Nath.jpg"}]
     */

    private int success;
    private String message;
    private List<BannerBean> banner;
    private List<CategoriesBean> categories;
    private List<LatestProductsBean> latestProducts;
    private List<SaleProductsBean> saleProducts;
    private List<TestimonialsBean> testimonials;

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

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public List<LatestProductsBean> getLatestProducts() {
        return latestProducts;
    }

    public void setLatestProducts(List<LatestProductsBean> latestProducts) {
        this.latestProducts = latestProducts;
    }

    public List<SaleProductsBean> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProductsBean> saleProducts) {
        this.saleProducts = saleProducts;
    }

    public List<TestimonialsBean> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(List<TestimonialsBean> testimonials) {
        this.testimonials = testimonials;
    }

    public static class BannerBean {
        /**
         * image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/front/images/appBanners/banner_.jpg
         * text : [{"line":""},{"line":""}]
         */

        private String image;
        private List<TextBean> text;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<TextBean> getText() {
            return text;
        }

        public void setText(List<TextBean> text) {
            this.text = text;
        }

        public static class TextBean {
            /**
             * line :
             */

            private String line;

            public String getLine() {
                return line;
            }

            public void setLine(String line) {
                this.line = line;
            }
        }
    }

    public static class CategoriesBean {
        /**
         * id : 1
         * name : Bridal Collection
         * image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/bridal_mobile.jpg
         * banner_image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/categories/banner/1496647971_bridals.jpg
         */

        private String id;
        private String name;
        private String image;
        private String banner_image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBanner_image() {
            return banner_image;
        }

        public void setBanner_image(String banner_image) {
            this.banner_image = banner_image;
        }
    }

    public static class LatestProductsBean {
        /**
         * is_in_wishlist : 0
         * is_out_of_stock : 0
         * id : 3895
         * name : Green Banarasi Woven Saree
         * image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473552_SR-SHA06-0074.jpg
         * price : {"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}
         */

        private int is_in_wishlist;
        private int is_out_of_stock;
        private String id;
        private String name;
        private String image;
        private PriceBean price;

        public int getIs_in_wishlist() {
            return is_in_wishlist;
        }

        public void setIs_in_wishlist(int is_in_wishlist) {
            this.is_in_wishlist = is_in_wishlist;
        }

        public int getIs_out_of_stock() {
            return is_out_of_stock;
        }

        public void setIs_out_of_stock(int is_out_of_stock) {
            this.is_out_of_stock = is_out_of_stock;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public static class PriceBean {
            /**
             * min_mrp_price : 1490
             * max_mrp_price : 1490
             * min_price : 745
             * max_price : 745
             */

            private int min_mrp_price;
            private int max_mrp_price;
            private int min_price;
            private int max_price;

            public int getMin_mrp_price() {
                return min_mrp_price;
            }

            public void setMin_mrp_price(int min_mrp_price) {
                this.min_mrp_price = min_mrp_price;
            }

            public int getMax_mrp_price() {
                return max_mrp_price;
            }

            public void setMax_mrp_price(int max_mrp_price) {
                this.max_mrp_price = max_mrp_price;
            }

            public int getMin_price() {
                return min_price;
            }

            public void setMin_price(int min_price) {
                this.min_price = min_price;
            }

            public int getMax_price() {
                return max_price;
            }

            public void setMax_price(int max_price) {
                this.max_price = max_price;
            }
        }
    }

    public static class SaleProductsBean {
        /**
         * is_in_wishlist : 0
         * is_out_of_stock : 0
         * id : 3891
         * name : Light Pink Woven Print Saree
         * image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/template/images/products/1529473268_SR-SHA06-0062.jpg
         * price : {"min_mrp_price":1490,"max_mrp_price":1490,"min_price":745,"max_price":745}
         */

        private int is_in_wishlist;
        private int is_out_of_stock;
        private String id;
        private String name;
        private String image;
        private PriceBeanX price;

        public int getIs_in_wishlist() {
            return is_in_wishlist;
        }

        public void setIs_in_wishlist(int is_in_wishlist) {
            this.is_in_wishlist = is_in_wishlist;
        }

        public int getIs_out_of_stock() {
            return is_out_of_stock;
        }

        public void setIs_out_of_stock(int is_out_of_stock) {
            this.is_out_of_stock = is_out_of_stock;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public PriceBeanX getPrice() {
            return price;
        }

        public void setPrice(PriceBeanX price) {
            this.price = price;
        }

        public static class PriceBeanX {
            /**
             * min_mrp_price : 1490
             * max_mrp_price : 1490
             * min_price : 745
             * max_price : 745
             */

            private int min_mrp_price;
            private int max_mrp_price;
            private int min_price;
            private int max_price;

            public int getMin_mrp_price() {
                return min_mrp_price;
            }

            public void setMin_mrp_price(int min_mrp_price) {
                this.min_mrp_price = min_mrp_price;
            }

            public int getMax_mrp_price() {
                return max_mrp_price;
            }

            public void setMax_mrp_price(int max_mrp_price) {
                this.max_mrp_price = max_mrp_price;
            }

            public int getMin_price() {
                return min_price;
            }

            public void setMin_price(int min_price) {
                this.min_price = min_price;
            }

            public int getMax_price() {
                return max_price;
            }

            public void setMax_price(int max_price) {
                this.max_price = max_price;
            }
        }
    }

    public static class TestimonialsBean {
        /**
         * name : Sunny Shah
         * comment : Fast as usual. Thanks.
         * image : http://www.shayonastore.com/image-tool/index.php?src=http://www.shayonastore.com/uploads/testimonial/1496906690_Sunny_Shah.jpg
         */

        private String name;
        private String comment;
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
