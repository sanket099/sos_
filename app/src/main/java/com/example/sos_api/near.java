package com.example.sos_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class near extends AppCompatActivity  implements OnMapReadyCallback {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }
    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);

        double lat = Double.parseDouble(shared_pref.getInstance(this).get_saved_lat());
        double longi = Double.parseDouble(shared_pref.getInstance(this).get_saved_longi());

        Call<nearbylist> call  = retrofit_client.getInstance().getapi().getnear(String.valueOf(lat),String.valueOf(longi));
        call.enqueue(new Callback<nearbylist>() {
            @Override
            public void onResponse(Call<nearbylist> call, Response<nearbylist> response) {
                //loc_response locResponse = response.body();
                /*assert locResponse != null;
                if(locResponse.isSuccess()){
                    Toast.makeText(near.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();

                    for (int i =0;i<locResponse.)
                }*/
//                String str = null;
//                try {
//                    assert response.body() != null;
//                    str = response.body().string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                JSONObject jsonObject = null;
//                try {
//                    assert str != null;
//                    jsonObject = new JSONObject(str);
//                    //System.out.println(jsonObject.getJSONArray("result"));
//                    JSONArray array = new JSONArray();
//                    array = jsonObject.getJSONArray("result");
//                    for (int i = 0 ; i<array.length() ; i++){
//                        //System.out.println(array.get(i));
//                        String string2 = array.get(i).toString();
//
//
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                nearbylist nearme = response.body();
                List<nearby> ans = nearme.getAns();

                for(int i = 0; i < ans.size() ; i++) {
                    System.out.println(i);
                    System.out.println(ans.get(i).getLat());
                    System.out.println(ans.get(i).getLongi());
                    System.out.println(ans.get(i).getUid());

                    mapboxMap.addMarker(new MarkerOptions().position
                            (new LatLng(Double.parseDouble(ans.get(i).getLat()) , Double.parseDouble(ans.get(i).getLat()) ))
                            .title("USER LOCATION \n" + "lat : " + ans.get(i).getLat() + "  long : " + ans.get(i).getLongi()));
                }

            }

            @Override
            public void onFailure(Call<nearbylist> call, Throwable t) {
                Toast.makeText(near.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        System.out.println(lat + " " + longi);

        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(
                lat, longi)).title("SOS LOCATION \n" + "lat : " + String.valueOf(lat) + "  long : " + longi));

        /* this is done for animating/moving camera to particular position */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat, longi)).zoom(10).tilt(0).build();
        mapboxMap.setCameraPosition(cameraPosition);
    }

    @Override
    public void onMapError(int i, String s) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
