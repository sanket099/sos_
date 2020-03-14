package com.example.sos_api;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.EventLog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class profile extends AppCompatActivity {
    TextView tv_details;
    TextView txtLat;
    Button sos;
    private LocationManager locationManager;

    private LocationListener listener;
    int c;
    TextToSpeech ts;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Intent mServiceIntent;
    public YourService mYourService;





   /* @Override
    protected void onStart() {
        super.onStart();

        if (!shared_pref.getInstance(this).is_logged_in()) {
            Intent intent = new Intent(profile.this, com.example.sos_api.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }*/


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //request();
        //sos = findViewById(R.id.btsos);
        pusher();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        tv_details = findViewById(R.id.tv_details);
       //pusher();

        sos = findViewById(R.id.high);
        /*dbHelper = new db_helper(this);
        lv =findViewById(R.id.lv);
        arrayList = new ArrayList<>(dbHelper.get_data());
*/
        //tv_details.setText( dbHelper.get_item_id(users.class.getName()).toString());
        //tv_details.setText("welcome"+arrayList.toString())
        //;
        //users users = shared_pref.getInstance(this).getuser();
        /*dbHelper = new db_helper(this);
        users users = dbHelper.get_data();*/
        // boolean b = dbHelper.isloggedin(users.getId());

        txtLat = findViewById(R.id.textview1);
        final users users = shared_pref.getInstance(this).getuser();
        final ArrayList<String> strings = shared_pref.getInstance(this).get_con();
        //final String value = shared_pref.getInstance(this).get_saved_user_id();
        tv_details.setText("Hi  " + users.getName() + " ! " /*+ " " + users.getPhone() + " " + users.getId() + " " + users.getAddress() + " " + users.getEmail()*/);
        //tv_details.setText("con" + strings);
        /*latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        /*if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }*/
        getLastLocation();
        registerForContextMenu(sos);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.level_low:
                low();
                return true;

            case R.id.level_medium:
                medium();
                return true;

            case R.id.high:
                high();
                return true;
            default:
                return super.onContextItemSelected(item);


        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.menu, menu2);
        return super.onCreateOptionsMenu(menu2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.callpol:
                call_pol();
                Toast.makeText(profile.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.shownearby:
                near();
                Toast.makeText(profile.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;






        //launchservice();

    }
    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }

    /*public void launchservice(){
        Intent i = new Intent(this,intent_service.class);
        i.putExtra("hello","yo");
        startService(i);
    }*/


    private void pusher() {
        //final String event_name;
        PusherOptions options = new PusherOptions().setCluster("ap2");

        Pusher pusher = new Pusher("8d9b4faca051e85c76ba",options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());


            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
            }
        }, ConnectionState.ALL);

        final Channel channel = pusher.subscribe("sos-channel", new ChannelEventListener() {         //sos
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("Subscribed!");
            }

            @Override
            public void onEvent(PusherEvent event) { //notification
                // Called for incoming events names "foo", "bar" or "baz"
               /* String s = event.getEventName();
                tv_details.setText(s+"");*/
               String channelid = "1";
              // event_name = event.getEventName();
                System.out.println(event.getData());
                Gson gson = new Gson();
                String string =  gson.toJson(event.getData());
                String s = null;
                s = event.getData();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    System.out.println(jsonObject.getString("coordinates"));
                    String levels = jsonObject.getString("level");
                    System.out.println(levels);
                    String[] abc = jsonObject.getString("coordinates").split(",");
                    //System.out.println(abc[1]);
                    String[] getlong = abc[0].split(":");
                   String longitude = (getlong[1]);
                   shared_pref.getInstance(profile.this).save_longitude_event(longitude);
                    String[] getlat = abc[1].split(":");
                    String lattitude = getlat[1].replace("}","");
                    shared_pref.getInstance(profile.this).save_lattitude_event(lattitude);

                    //System.out.println(lattitude);
                    //System.out.println(longitude);
                    if (!isMyServiceRunning(mYourService.getClass())) {
                        startService(mServiceIntent);
                    }

                    NotificationManagerCompat notificationManagerCompat;
                    notificationManagerCompat = NotificationManagerCompat.from(profile.this);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel1 = new NotificationChannel(
                                "channel1","channel1", NotificationManager.IMPORTANCE_DEFAULT
                        );
                        String loc = "http://maps.google.com/maps?saddr="+lattitude+","+longitude;
                        channel1.setDescription("http://maps.google.com/maps?saddr="+lattitude+","+longitude);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel1);

                    }
                    ts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                ts.setLanguage(Locale.getDefault());
                            }
                            else{
                                Toast.makeText(profile.this,"error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    String loc = "http://maps.google.com/maps?saddr="+lattitude+","+longitude;
                    Context context = getApplicationContext();
                    Intent myIntent = new Intent(context, map.class);
                    PendingIntent intent = PendingIntent.getActivity(context,1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);



                    if (levels.equals("high")){
                        if (!isMyServiceRunning(mYourService.getClass())) {
                            startService(mServiceIntent);
                        }
                        Notification notification = new NotificationCompat.Builder(profile.this,"channel1").setContentTitle("HIGH ALERT! get assistance! Quick!").setContentText(loc)
                                .setSmallIcon(R.drawable.icon)
                                .setPriority(4).setCategory(NotificationCompat.CATEGORY_MESSAGE).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                                .setContentIntent(intent)
                                .build();

                        notificationManagerCompat.notify(1,notification);
                        ts.speak(String.valueOf("HELP!"),TextToSpeech.QUEUE_FLUSH,null);

                    }
                    else if (levels.equals("medium")){
                        if (!isMyServiceRunning(mYourService.getClass())) {
                            startService(mServiceIntent);
                        }
                        Notification notification = new NotificationCompat.Builder(profile.this,"channel1").setContentTitle("Medium Alert! Proceed fast").setContentText(loc)
                                .setSmallIcon(R.drawable.icon)
                                .setPriority(4).setCategory(NotificationCompat.CATEGORY_MESSAGE).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                                .setContentIntent(intent)
                                .build();

                        notificationManagerCompat.notify(1,notification);
                        ts.speak(String.valueOf("HELP!"),TextToSpeech.QUEUE_FLUSH,null);

                    }
                    else if (levels.equals("low")){
                        if (!isMyServiceRunning(mYourService.getClass())) {
                            startService(mServiceIntent);
                        }
                        Notification notification = new NotificationCompat.Builder(profile.this,"channel1").setContentTitle("low alert! you may proceed alone").setContentText(loc)
                                .setSmallIcon(R.drawable.icon)
                                .setPriority(4).setCategory(NotificationCompat.CATEGORY_MESSAGE).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                                .setContentIntent(intent)
                                .build();

                        notificationManagerCompat.notify(1,notification);
                        ts.speak(String.valueOf("HELP!"),TextToSpeech.QUEUE_FLUSH,null);

                    }
                    /*Notification notification = new NotificationCompat.Builder(profile.this,"channel1").setContentTitle("help").setContentText(loc)
                            .setSmallIcon(R.drawable.icon)
                            .setPriority(4).setCategory(NotificationCompat.CATEGORY_MESSAGE).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                            .setContentIntent(intent)
                            .build();

                    notificationManagerCompat.notify(1,notification);
                    ts.speak(String.valueOf("HELP!"),TextToSpeech.QUEUE_FLUSH,null);*/


                   /* Intent notificationIntent = new Intent(context, profile.class);

                   *//* notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent intent = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);

                   // notification.setLatestEventInfo(context, title, message, intent);
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;*//*
                    */

                   /* PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                            new Intent(this, com.example.sos_api.profile.class), PendingIntent.FLAG_UPDATE_CURRENT);



                    notification.contentIntent = contentIntent;

*/




                    //System.out.println(abc[0]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //notification

                //nearby channel - data - act - "sos incoming"


            }
        }, "common");
        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
        /*final Channel channel1 = pusher.subscribe("nearby-channel", new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                System.out.println("Subscribed! to nearby channel");

            }

            @Override
            public void onEvent(PusherEvent event) {
                System.out.println(event.getData());
                String s = event.getEventName();

            }
        },value);*/

    }

    @SuppressLint("MissingPermission")
        private void getLastLocation() {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    final Location location = task.getResult();
                                    if (location == null) {
                                        requestNewLocationData();
                                    } else {
                                        //txtLat.setText(location.getLatitude() + "  " + location.getLongitude() + "");
                                        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
                                        Call<login_response> call = retrofit_client.getInstance().getapi().postloc(value, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                                        call.enqueue(new Callback<login_response>() {
                                            @Override
                                            public void onResponse(Call<login_response> call, Response<login_response> response) {
                                                login_response loginResponse = response.body();


                                                if (loginResponse.isSuccess()) {

                                                    shared_pref.getInstance(profile.this).save_lat(String.valueOf(location.getLatitude()));
                                                    shared_pref.getInstance(profile.this).save_longi(String.valueOf(location.getLongitude()));
                                                    //pusher();
                                                    //Toast.makeText(profile.this, loginResponse.getMsg(), Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<login_response> call, Throwable t) {
                                                Toast.makeText(profile.this, t.getMessage() + "error", Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    }
                                }


                            });
                }
                else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
                }
            else {
                requestPermissions();
            }



        }



        @SuppressLint("MissingPermission")
        private void requestNewLocationData(){

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest, mLocationCallback,
                    Looper.myLooper()
            );

        }

        private LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                txtLat.setText(mLastLocation.getLatitude()+"  "+mLastLocation.getLongitude()+"  ");

            }
        };

        private boolean checkPermissions() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }

        private void requestPermissions() {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE},
                    PERMISSION_ID
            );
        }

        private boolean isLocationEnabled() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
            );
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == PERMISSION_ID) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                    call_pol();
                }
            }
        }

        @Override
        public void onResume(){
            super.onResume();
            if (checkPermissions()) {
                getLastLocation();
            }

        }


    /*public void sos(View view) {
            pusher();
            String saved_lat = shared_pref.getInstance(this).get_saved_lat();
            String saved_longi = shared_pref.getInstance(this).get_saved_longi();
            if(checkPermissions()){
                send_sms();
            }
            else{
                requestPermissions();
            }



        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
            Call<loc_response> call = retrofit_client.getInstance().getapi().getuserloc(value,saved_lat,saved_longi,"10000","high");
            call.enqueue(new Callback<loc_response>() {
                @Override
                public void onResponse(Call<loc_response> call, Response<loc_response> response) {
                    loc_response locResponse = response.body();
                    assert locResponse != null;
                    if(locResponse.isSuccess()){
                        Toast.makeText(profile.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();
                    }



                }

                @Override
                public void onFailure(Call<loc_response> call, Throwable t) {
                    Toast.makeText(profile.this,"error " + t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
    }
*/
    private void send_sms() {

        SmsManager smsManager = SmsManager.getDefault();
        final ArrayList<String> st = shared_pref.getInstance(this).get_con();
        String lat = shared_pref.getInstance(this).get_saved_lat();
        String lon = shared_pref.getInstance(this).get_saved_longi();
        String message="http://maps.google.com/maps?saddr="+lat+","+lon;
        String number = st.get(2); // send to all not one
        String number2 = st.get(6);
        String number3 = st.get(10);

        if(!number2.isEmpty()) {
            smsManager.sendTextMessage(number2,null,message,null,null);
            Toast.makeText(profile.this,"sent sos sms to  "+number2,Toast.LENGTH_LONG).show();
        }

        if(!number3.isEmpty()) {
            smsManager.sendTextMessage(number3,null,message,null,null);
            Toast.makeText(profile.this,"sent sos sms to  "+number3,Toast.LENGTH_LONG).show();
        }

        smsManager.sendTextMessage(number,null,message,null,null);
        Toast.makeText(profile.this,"sent sos sms to  "+number,Toast.LENGTH_LONG).show();

    }

    public void near() {
            startActivity(new Intent(this,com.example.sos_api.near.class));


    }

    public void high() {
        pusher();
        String saved_lat = shared_pref.getInstance(this).get_saved_lat();
        String saved_longi = shared_pref.getInstance(this).get_saved_longi();
        if(checkPermissions()){
            send_sms();
        }
        else{
            requestPermissions();
        }



        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
        Call<loc_response> call = retrofit_client.getInstance().getapi().getuserloc(value,saved_lat,saved_longi,"10000","high");
        call.enqueue(new Callback<loc_response>() {
            @Override
            public void onResponse(Call<loc_response> call, Response<loc_response> response) {
                loc_response locResponse = response.body();
                assert locResponse != null;
                if(locResponse.isSuccess()){
                    Toast.makeText(profile.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();
                }



            }

            @Override
            public void onFailure(Call<loc_response> call, Throwable t) {
                Toast.makeText(profile.this,"error " + t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }



    public void low() {
        pusher();
        String saved_lat = shared_pref.getInstance(this).get_saved_lat();
        String saved_longi = shared_pref.getInstance(this).get_saved_longi();
        if(checkPermissions()){
            send_sms();
        }
        else{
            requestPermissions();
        }



        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
        Call<loc_response> call = retrofit_client.getInstance().getapi().getuserloc(value,saved_lat,saved_longi,"10000","low");
        call.enqueue(new Callback<loc_response>() {
            @Override
            public void onResponse(Call<loc_response> call, Response<loc_response> response) {
                loc_response locResponse = response.body();
                assert locResponse != null;
                if(locResponse.isSuccess()){
                    Toast.makeText(profile.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();
                }



            }

            @Override
            public void onFailure(Call<loc_response> call, Throwable t) {
                Toast.makeText(profile.this,"error " + t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }



    public void medium() {
        pusher();
        String saved_lat = shared_pref.getInstance(this).get_saved_lat();
        String saved_longi = shared_pref.getInstance(this).get_saved_longi();
        if(checkPermissions()){
            send_sms();
        }
        else{
            requestPermissions();
        }



        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
        Call<loc_response> call = retrofit_client.getInstance().getapi().getuserloc(value,saved_lat,saved_longi,"10000","medium");
        call.enqueue(new Callback<loc_response>() {
            @Override
            public void onResponse(Call<loc_response> call, Response<loc_response> response) {
                loc_response locResponse = response.body();
                assert locResponse != null;
                if(locResponse.isSuccess()){
                    Toast.makeText(profile.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();
                }



            }

            @Override
            public void onFailure(Call<loc_response> call, Throwable t) {
                Toast.makeText(profile.this,"error " + t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }


   /* public void call_police(View view) {
        call_pol();


    }*/
    public void call_pol(){
        if(ContextCompat.checkSelfPermission(profile.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(profile.this,new String[] {Manifest.permission.CALL_PHONE},1);

        }
        else{
            String num = "tel:+919315839038";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(num)));
        }


    }

    public void sos(View view) {

        pusher();
        String saved_lat = shared_pref.getInstance(this).get_saved_lat();
        String saved_longi = shared_pref.getInstance(this).get_saved_longi();
        if(checkPermissions()){
            send_sms();
        }
        else{
            requestPermissions();
        }



        final String value = shared_pref.getInstance(profile.this).get_saved_user_id();
        Call<loc_response> call = retrofit_client.getInstance().getapi().getuserloc(value,saved_lat,saved_longi,"10000","high");
        call.enqueue(new Callback<loc_response>() {
            @Override
            public void onResponse(Call<loc_response> call, Response<loc_response> response) {
                loc_response locResponse = response.body();
                assert locResponse != null;
                if(locResponse.isSuccess()){
                    Toast.makeText(profile.this,"ok "+locResponse.msg,Toast.LENGTH_LONG ).show();
                }



            }

            @Override
            public void onFailure(Call<loc_response> call, Throwable t) {
                Toast.makeText(profile.this,"error " + t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}






       /* if (ContextCompat.checkSelfPermission(profile.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(profile.this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(profile.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION )||ActivityCompat.shouldShowRequestPermissionRationale(profile.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION )) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(profile.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                                c = 0;

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(profile.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
       } else {
            // Permission has already been granted
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Double latittude = location.getLatitude();
                                Double longitude = location.getLongitude();


                                txtLat.setText("Latitude = " + latittude + "\nLongitude = " + longitude);

*/
                               /* String ab = String.valueOf(users.getId());

                                Call<login_response> call = retrofit_client.getInstance().getapi().postloc(value,latittude.toString(),longitude.toString());
                                call.enqueue(new Callback<login_response>() {
                                    @Override
                                    public void onResponse(Call<login_response> call, Response<login_response> response) {
                                        login_response loginResponse = response.body();

                                        if(loginResponse.isSuccess()){
                                            Toast.makeText(profile.this,loginResponse.getMsg(),Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<login_response> call, Throwable t) {
                                        Toast.makeText(profile.this,t.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        }*/



/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //abc
            } else {

            }
        }
    }
}
*/
