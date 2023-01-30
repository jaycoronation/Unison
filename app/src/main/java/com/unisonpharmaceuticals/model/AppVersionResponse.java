package com.unisonpharmaceuticals.model;

/**
 * Created by Kiran Patel on 20-Dec-18.
 */
public class AppVersionResponse
{

    /**
     * version : 2.1.2
     * force_update : 1
     * success : 1
     * message : Android version found!
     */

    private String version = "";
    private String force_update = "";
    private int success = 0;
    private String message = "";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String force_update) {
        this.force_update = force_update;
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
}
