package com.example.sos_api;

public class login_response {
    public boolean success;
    public String uid;
    public String msg;
    public users result;


    public login_response(boolean success, String uid, String msg, users result) {
        this.success = success;
        this.uid = uid;
        this.msg = msg;
        this.result = result;
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

    public users getUsers() {

        return result;
    }
}
