package com.unisonpharmaceuticals.model;

import java.util.List;

/**
 * Created by Tushar Vataliya on 21-Jun-18.
 */
public class NavigationGeSet
{

    /**
     * categories : [{"id":"1","name":"Bridal Collection","childrens":[{"id":"15","name":"Bridal Lahenga "}]},{"id":"2","name":"Saree","childrens":[{"id":"43","name":"Kanchipuram Pure Silk "},{"id":"50","name":"Silk Saree"},{"id":"51","name":"Catalog Saree"},{"id":"8","name":" Bandhani Saree "},{"id":"10","name":"Patola Saree"},{"id":"42","name":"Banarasi Silk Saree"}]},{"id":"3","name":"Dresses","childrens":[{"id":"44","name":"Gown Style Dress"},{"id":"39","name":"Crop Top"},{"id":"41","name":"Duppatta Top"}]},{"id":"4","name":"Chaniya Choli","childrens":[{"id":"17","name":"Navratri"},{"id":"18","name":"Wedding "},{"id":"19","name":"Reception "},{"id":"20","name":"Party "},{"id":"21","name":"Sangeet"},{"id":"22","name":"Silk Chaniyacholi"}]},{"id":"5","name":"Gown"},{"id":"23","name":"Men","childrens":[{"id":"38","name":"Kurta Set"},{"id":"47","name":"Blazers"},{"id":"52","name":"Indowestern"},{"id":"58","name":"Jacket Kurta Set"},{"id":"59","name":"Jodhpuri"},{"id":"60","name":"Sherwani"}]}]
     * account : [{"name":"Profile","slug":"profile"},{"name":"My Wallet","slug":"my-wallet"},{"name":"Order History","slug":"order-history"},{"name":"Favourite Items","slug":"favourite-items"},{"name":"Address Book","slug":"address-book"},{"name":"Change Password","slug":"change-password"},{"name":"Reviews & Ratings","slug":"reviews-and-ratings"},{"name":"Newsletter","slug":"newsletter"},{"name":"Submit Query","slug":"submit-query"}]
     * success : 1
     * message : 28 categories found
     */

    private int success;
    private String message;
    private List<CategoriesBean> categories;
    private List<AccountBean> account;

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

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public List<AccountBean> getAccount() {
        return account;
    }

    public void setAccount(List<AccountBean> account) {
        this.account = account;
    }

    public static class CategoriesBean {
        /**
         * id : 1
         * name : Bridal Collection
         * childrens : [{"id":"15","name":"Bridal Lahenga "}]
         */

        private String id;
        private String name;
        private List<ChildrensBean> childrens;

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

        public List<ChildrensBean> getChildrens() {
            return childrens;
        }

        public void setChildrens(List<ChildrensBean> childrens) {
            this.childrens = childrens;
        }

        public static class ChildrensBean {
            /**
             * id : 15
             * name : Bridal Lahenga
             */

            private String id;
            private String name;

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
        }
    }

    public static class AccountBean {
        /**
         * name : Profile
         * slug : profile
         */

        private String name;
        private String slug;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }
}
