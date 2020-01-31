package com.example.sos_api;

public class loc_response {
    public boolean success;
    public String uid;
    public String msg;
    public String lat;
    public String longi;

    public loc_response(boolean success, String uid, String msg, String lat, String longi) {
        this.success = success;
        this.uid = uid;
        this.msg = msg;
        this.lat = lat;
        this.longi = longi;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUid() {
        return uid;
    }

    public String getMsg() {
        return msg;
    }

    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }
}
