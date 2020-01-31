package com.example.sos_api;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mmi.services.account.MapmyIndiaAccountManager;

public class application_class extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());
        MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());
        MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType(getAtlasGrantType());
        MapmyIndia.getInstance(this);
    }

    private String getAtlasClientId() {
        return "qelfXabVgBuyotclkXRLzEomeez-f5CW3AmU258E8B5SkI7Wkp6z945b0YW1uUUSzoa_GrN6QTnjqWyqWH9I_A==";
    }

    private String getAtlasGrantType() {
        return "client_credentials";
    }

    private String getMapSDKKey() {
        return "va2rxxoyjh6yhfqp2wdu86awjw21e6a9";
    }

    private String getAtlasClientSecret() {
        return "NdJUAD9O1c0cUi43KEdTMxrnsYUCapvR_neOQddhbxovybIju12qJYH-F4k0LGyPWVh3PXgubBQirI-PVE8jilQ7L8pHsmZ2";
    }

    private String getRestAPIKey() {
        return "sdtswbkkpmt73naswatotch3dent565a";


    }




}
