package com.example.sos_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class nearbylist {
    @SerializedName("ans")
    private List<nearby> ans;

    public List<nearby> getAns() {
        return ans;
    }

    public void setAns(List<nearby> ans) {
        this.ans = ans;
    }

}
