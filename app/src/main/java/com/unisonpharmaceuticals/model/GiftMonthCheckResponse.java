package com.unisonpharmaceuticals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Patel on 09-Aug-18.
 */
public class GiftMonthCheckResponse
{


    /**
     * plan : [{"map_id":"86","item":"AIR FRESHENER (HARMONY) (AFR1)","type":"Special Gift"}]
     * success : 1
     * message :
     */

    private int success = 0;
    private String message = "";
    private List<PlanBean> plan = new ArrayList<>();

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

    public List<PlanBean> getPlan() {
        return plan;
    }

    public void setPlan(List<PlanBean> plan) {
        this.plan = plan;
    }

    public static class PlanBean {
        /**
         * map_id : 86
         * item : AIR FRESHENER (HARMONY) (AFR1)
         * type : Special Gift
         */

        private String map_id = "";
        private String item = "";
        private String type = "";

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
