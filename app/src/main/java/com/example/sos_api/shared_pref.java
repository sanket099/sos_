package com.example.sos_api;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class shared_pref {
    private static final String shared_pref_name = "my_shared_pref";

    private static shared_pref instance;
    private Context context;

    private shared_pref(Context context) {
        this.context = context;
    }

    public static synchronized shared_pref getInstance(Context context) {
        if (instance == null) {
            instance = new shared_pref(context);

        }
        return instance;
    }

    public void save_user(users users) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", users.getId());
        editor.putString("name", users.getName());
        editor.putString("email", users.getEmail());
        editor.putString("phone", users.getPhone());
        editor.putString("address", users.getAddress());
        editor.apply();
    }

    public boolean is_logged_in() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("id", -1) != -1) {
            return true;
        }
        return false;
    }

    public users getuser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        users users = new users(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("phone", null),
                sharedPreferences.getString("address", null)
        );
        return users;
    }

    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


    }

    public void save_user_id(String uid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid);
        editor.apply();


    }

    public String get_saved_user_id() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("uid", "0");
        return value;


    }

    public void save_contacts(String name, String email, String phnno, String address, String name2, String email2, String phnno2, String address2, String name3, String email3, String phnno3, String address3) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("con_name", name);
        editor.putString("con_email", email);
        editor.putString("con_phnno", phnno);
        editor.putString("con_Add", address);
        editor.putString("con_name2", name2);
        editor.putString("con_email2", email2);
        editor.putString("con_phnno2", phnno2);
        editor.putString("con_Add2", address2);
        editor.putString("con_name3", name3);
        editor.putString("con_email3", email3);
        editor.putString("con_phnno3", phnno3);
        editor.putString("con_Add3", address3);
        editor.apply();
    }

    public ArrayList<String> get_con() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String con_name = sharedPreferences.getString("con_name", "0");
        String con_email = sharedPreferences.getString("con_email", "0");
        String con_phnno = sharedPreferences.getString("con_phnno", "0");
        String con_add = sharedPreferences.getString("con_Add", "0");
        String con_name2 = sharedPreferences.getString("con_name2", "0");
        String con_email2 = sharedPreferences.getString("con_email2", "0");
        String con_phnno2 = sharedPreferences.getString("con_phnno2", "0");
        String con_add2 = sharedPreferences.getString("con_Add2", "0");
        String con_name3 = sharedPreferences.getString("con_name3", "0");
        String con_email3 = sharedPreferences.getString("con_email3", "0");
        String con_phnno3 = sharedPreferences.getString("con_phnno3", "0");
        String con_add3 = sharedPreferences.getString("con_Add3", "0");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(con_name);
        arrayList.add(con_email);
        arrayList.add(con_phnno);
        arrayList.add(con_add);
        arrayList.add(con_name2);
        arrayList.add(con_email2);
        arrayList.add(con_phnno2);
        arrayList.add(con_add2);
        arrayList.add(con_name3);
        arrayList.add(con_email3);
        arrayList.add(con_phnno3);
        arrayList.add(con_add3);

        return arrayList;

    }

    public void save_lat(String lat) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latti", lat);

        editor.apply();
    }

    public void save_longi(String longi) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("longi", longi);

        editor.apply();
    }

    public String get_saved_lat() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String loc_lat = sharedPreferences.getString("latti", "0");

        return loc_lat;

    }

    public String get_saved_longi() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String loc_longi = sharedPreferences.getString("longi", "0");

        return loc_longi;
    }
    public void save_lattitude_event(String lat) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latti_event", lat);

        editor.apply();
    }
    public void save_longitude_event(String lon) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("longi_event", lon);

        editor.apply();
    }
    public String get_saved_lattitude_event() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String loc_lat = sharedPreferences.getString("latti_event", "0");

        return loc_lat;

    }

    public String get_saved_longi_event() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE);
        String loc_longi = sharedPreferences.getString("longi_event", "0");

        return loc_longi;
    }
}
