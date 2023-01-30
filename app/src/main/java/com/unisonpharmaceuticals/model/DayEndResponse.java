package com.unisonpharmaceuticals.model;

/**
 * Created by Tushar Vataliya on 06-Jul-18.
 */
public class DayEndResponse
{

    /**
     * success : 1
     * message : Report has been updated successfully.
     */

    private int success;
    private String message;

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
}
