package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 20-Jul-18.
 */
public class CommonResponse
{

    /**
     * success : 1
     * message : Data successfully sent.
     */

    private int success=0;
    private String message = "";
    private String status = "";

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
