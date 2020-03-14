package com.example.sos_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class login_activityy extends AppCompatActivity {
    EditText et_login_email;
    EditText et_login_pass;
    Button btn_login;
    db_helper dbHelper;
    TextView textView;
    EditText et_name,et_name2,et_name3,et_phnno,et_phnno2,et_phnno3,et_email,et_email2,et_email3,et_city,et_city2,et_city3;
    Button sos , save;
    TextView tv1,tv2,tv3;


    @Override
    protected void onStart() {
        super.onStart();
        if (shared_pref.getInstance(this).is_logged_in()){
            Intent intent = new Intent(login_activityy.this,com.example.sos_api.profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activityy);
        et_login_email = findViewById(R.id.email_login);
        et_login_pass = findViewById(R.id.password_login);
        textView = findViewById(R.id.tvloginerror);
        tv1 = findViewById(R.id.tvcontact1);
        tv2 = findViewById(R.id.tvcontact2);
        tv3 = findViewById(R.id.tvcontact3);
        btn_login = findViewById(R.id.login_bt);
        //dbHelper = new db_helper(this);

        //emergency contacts

        et_name = findViewById(R.id.name);
        et_name2 = findViewById(R.id.name2);
        et_name3 = findViewById(R.id.name3);

        et_phnno = findViewById(R.id.phnno);
        et_phnno2 = findViewById(R.id.phnno2);
        et_phnno3 = findViewById(R.id.phnno3);
        et_email = findViewById(R.id.email);
        et_email2 = findViewById(R.id.email2);
        et_email3 = findViewById(R.id.email3);
        save = findViewById(R.id.savecon);
        et_city = findViewById(R.id.city);
        et_city2 = findViewById(R.id.city2);
        et_city3 = findViewById(R.id.city3);

        et_name.setVisibility(View.INVISIBLE);
        et_name2.setVisibility(View.INVISIBLE);
        et_name3.setVisibility(View.INVISIBLE);
        et_phnno.setVisibility(View.INVISIBLE);
        et_phnno2.setVisibility(View.INVISIBLE);
        et_phnno3.setVisibility(View.INVISIBLE);
        et_email.setVisibility(View.INVISIBLE);
        et_email2.setVisibility(View.INVISIBLE);
        et_email3.setVisibility(View.INVISIBLE);
        et_city.setVisibility(View.INVISIBLE);
        et_city2.setVisibility(View.INVISIBLE);
        et_city3.setVisibility(View.INVISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);






        //-------------------------------------------------------------------------
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login_emaail = et_login_email.getText().toString().trim();
                String login_password = et_login_pass.getText().toString().trim();
                Call<login_response> call = retrofit_client.getInstance().getapi().userlogin(login_emaail,login_password);

                call.enqueue(new Callback<login_response>() {
                    @Override
                    public void onResponse(Call<login_response> call, Response<login_response> response) {

                        login_response loginResponse = response.body();
                        assert loginResponse != null;
                        if(loginResponse.isSuccess()){
                            //shared_pref.getInstance(login_activityy.this).save_user(loginResponse.getUsers());
                            //String s = shared_pref.getInstance(login_activityy.this).getuser().getName().toString();
                            //ISLOGGEDIN
                            //int a = Integer.parseInt(loginResponse.getUid());
                            String a = loginResponse.getUid();
                            shared_pref.getInstance(login_activityy.this).save_user_id(a);

                            Call<login_response> call1 = retrofit_client.getInstance().getapi().getuser(a);
                            call1.enqueue(new Callback<login_response>() {
                                @Override
                                public void onResponse(Call<login_response> call, Response<login_response> response1) {
                                    login_response loginResponse1 = response1.body();
                                    //assert loginResponse1 != null;
                                    /*if(response1.code()==200){
                                        textView.setText(""+response1.body());

                                    }*/
                                    if(loginResponse1.isSuccess()){




                                        shared_pref.getInstance(login_activityy.this).save_user(loginResponse1.getUsers());
                                        et_login_email.setVisibility(View.GONE);
                                        et_login_pass.setVisibility(View.GONE);
                                        btn_login.setVisibility(View.GONE);
                                        et_name.setVisibility(View.VISIBLE);
                                        et_name2.setVisibility(View.VISIBLE);
                                        et_name3.setVisibility(View.VISIBLE);
                                        et_phnno.setVisibility(View.VISIBLE);
                                        et_phnno2.setVisibility(View.VISIBLE);
                                        et_phnno3.setVisibility(View.VISIBLE);
                                        et_email.setVisibility(View.VISIBLE);
                                        et_email2.setVisibility(View.VISIBLE);
                                        et_email3.setVisibility(View.VISIBLE);
                                        et_city.setVisibility(View.VISIBLE);
                                        et_city2.setVisibility(View.VISIBLE);
                                        et_city3.setVisibility(View.VISIBLE);
                                        tv1.setVisibility(View.VISIBLE);
                                        tv2.setVisibility(View.VISIBLE);
                                        tv3.setVisibility(View.VISIBLE);
                                       /* shared_pref.getInstance(login_activityy.this).save_contacts(name_2,email_2,phnno_2,city_2);
                                        shared_pref.getInstance(login_activityy.this).save_contacts(name_3,email_3,phnno_3,city_3);
*/

                                       save.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               String name_ = et_name.getText().toString();
                                               String name_2 = et_name2.getText().toString();
                                               String name_3 = et_name3.getText().toString();
                                               String phnno_ = et_phnno.getText().toString();
                                               String phnno_2 = et_phnno2.getText().toString();
                                               String phnno_3 = et_phnno3.getText().toString();
                                               String email_ = et_email.getText().toString();
                                               String email_2 = et_email2.getText().toString();
                                               String email_3 = et_email3.getText().toString();
                                               String city_ = et_city.getText().toString();
                                               String city_2 = et_city2.getText().toString();
                                               String city_3 = et_city3.getText().toString();
                                               if(name_.isEmpty() || phnno_.isEmpty() || email_.isEmpty()) {
                                                   Toast.makeText( login_activityy.this, "Please enter atleast 1 emergency contact", Toast.LENGTH_LONG).show();
                                               } else {
                                                   shared_pref.getInstance(login_activityy.this).save_contacts(name_,email_,phnno_,city_,name_2,email_2,phnno_2,city_2,name_3,email_3,phnno_3,city_3);

                                                   Intent intent = new Intent(login_activityy.this,com.example.sos_api.profile.class);

                                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                   startActivity(intent);
                                               }

                                           }
                                       });
                                        /*String obj_name = shared_pref.getInstance(login_activityy.this).getuser().getName().toString();
                                        String obj_email = shared_pref.getInstance(login_activityy.this).getuser().getEmail().toString();
                                        String obj_phone = shared_pref.getInstance(login_activityy.this).getuser().getPhone().toString();
                                        String obj_address = shared_pref.getInstance(login_activityy.this).getuser().getAddress().toString();
                                        textView.setText("name " + obj_name + " email " + obj_email + " phone " + obj_phone + " address " + obj_address);*/

                                    }

                                }

                                @Override
                                public void onFailure(Call<login_response> call, Throwable t) {

                                }
                            });

                            //textView.setText("yay"+loginResponse.getMsg()+loginResponse.getUsers());
                           /* startActivity(new Intent(login_activityy.this,com.example.sos_api.profile.class));*/

                        }
                        else{
                            Toast.makeText(login_activityy.this,"wrong password",Toast.LENGTH_LONG).show();
                            textView.setText(loginResponse.getMsg());
                        }

                        /*assert response.body() != null;
                        String string = null;
                        login_response login_response = response.body();
                        try {
                            if (response.code() == 200 ){
                                //string = response.body().string();




                            }
                            else{
                                assert response.errorBody() != null;
                                string = response.errorBody().string();

                            }
                            //string = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Gson gson = new Gson();
                        //String string =  gson.toJson(response.body());
                        //Toast.makeText(MainActivity.this,string,Toast.LENGTH_LONG).show();
                        if (string != null){
                            try{
                                JSONObject jsonObject = new JSONObject(string);
                                textView.setText(jsonObject.getString("msg"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (response.code()==200){
                            startActivity(new Intent(login_activityy.this,com.example.sos_api.profile.class));
                        }


*/
                    }

                    @Override
                    public void onFailure(Call<login_response> call, Throwable t) {

                    }
                });
            }
        });








    }
   /* private  void  add_data(String i1,String i2,String i3 ,String i4,String i5,String i6 ,String i7,String i8,String i9 ,String i10,String i11,String i12){
        boolean inser_data = dbHelper.adddata(i1, i2 , i3, i4, i5,i6,i7,i8,i9,i10,i11,i12);
        if (inser_data){
            //i = 1;
            //noteadapter.notifyDataSetChanged();
            sos.setVisibility(View.VISIBLE);

            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_LONG).show();


        }
        else{
            Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
        }
    }*/

}
