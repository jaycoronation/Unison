package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 17-Aug-18.
 */
public class GiftItemResponse
{


    /**
     * items : [{"map_id":"2","item":"ALL - Harmony (August)"}]
     * success : 1
     * message :
     */

    private int success = 0;
    private String message = "";
    private List<ItemsBean> items = new ArrayList<>();

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

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * map_id : 2
         * item : ALL - Harmony (August)
         */

        private String map_id = "";
        private String item = "";

        public String getMap_id() {
            return map_id;
        }

        public void setMap_id(String map_id) {
            this.map_id = map_id;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }
}
