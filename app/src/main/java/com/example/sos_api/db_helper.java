package com.example.sos_api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class db_helper extends SQLiteOpenHelper {

    public static  final  String table = "contact_table";
    public static final  String col1 = "ID";
    public static final  String col2 = "NAME";
    public   static final  String col3 = "EMAIL";
    public   static  final  String col4 = "PHONE";
    public  static  final  String col5 = "ADDRESS";
    private emerg_contacts emerg_contacts;

    public db_helper(@Nullable Context context) {
        super(context,table,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = " CREATE TABLE " + table + "( ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT , EMAIL TEXT, PHONE TEXT, ADDRESS TEXT )";
        db.execSQL(create_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + table);
        onCreate(db);

    }
    public boolean adddata(String name, String email, String phone, String address){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(col2,name);
        contentValues.put(col3,email);
        contentValues.put(col4,phone);
        contentValues.put(col5,address);



        long result = db.insert(table,null,contentValues);
        db.close();
        if (result == -1){
            return false;
        }
        else{
            return  true;
        }
    }

    public emerg_contacts get_data(){
        //ArrayList<users> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + table;
        emerg_contacts emergContacts = new emerg_contacts();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                emergContacts = new emerg_contacts();
                emergContacts.setContact_id(cursor.getInt(0));
                emergContacts.setContact_name(cursor.getString(1));
                emergContacts.setContact_email(cursor.getString(2));
                emergContacts.setContact_phone(cursor.getString(3));
                emergContacts.setContact_address(cursor.getString(4));
                //arrayList.add(user);
            }while (cursor.moveToNext());
        }
        return emergContacts;

    }
    public emerg_contacts get(int id){
        emerg_contacts emergContacts = new emerg_contacts();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + table + "WHERE" +col1+ "='" + id + "'";
        Cursor cursor = db.rawQuery(query,null);
        return emergContacts;

    }
    public  Cursor get_item_id (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + col1 + "FROM "+ table + "WHERE" + col2 + "='" + name + "'" ;
        Cursor cursor = db.rawQuery(query,null);
        return  cursor;
    }



}
