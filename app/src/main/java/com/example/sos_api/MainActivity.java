package com.example.sos_api;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText etname, etemail, etphn, etaddress, etpassword;
    Button btnregister;
    private api mapi;
    db_helper dbHelper;
    TextView tverror, gotologin;


    @Override
    protected void onStart() {
        super.onStart();
        if (shared_pref.getInstance(this).is_logged_in()){
            Intent intent = new Intent(MainActivity.this,com.example.sos_api.profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //register activity
        //first activity

        etname = findViewById(R.id.name);
        gotologin = findViewById(R.id.gotologin);
        etemail = findViewById(R.id.email);
        etphn = findViewById(R.id.phn);
        dbHelper =new db_helper(this);
        etaddress = findViewById(R.id.address
        );
        etpassword = findViewById(R.id.password);
        btnregister = findViewById(R.id.register);


        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.sos_api.login_activityy.class));
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etname.getText().toString().trim();
                final String email = etemail.getText().toString().trim();
                final String phone = etphn.getText().toString().trim();
                final String address = etaddress.getText().toString().trim();
                String password = etpassword.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "all fields are important", Toast.LENGTH_LONG).show();

                } else {
                    Call<ResponseBody> call = retrofit_client.getInstance().getapi().create_user(name, email, password, phone, address);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            assert response.body() != null;
                            String string = null;
                            try {
                                if (response.code() == 200) {
                                    string = response.body().string();
                                    Toast.makeText(MainActivity.this,"registered",Toast.LENGTH_LONG).show();
                                } else {
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
                            if (string != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(string);
                                    //tverror.setText(jsonObject.getString("msg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (response.code() == 200){
                                //add_data(name,email,phone,address);
                                startActivity(new Intent(MainActivity.this,com.example.sos_api.login_activityy.class));
                            }
                            //tverror.setText(string);

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "error" + t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }


            }
        });
    }
    private   void  add_data(String i1,String i2, String i3, String i4){
        boolean inser_data = dbHelper.adddata(i1, i2 , i3, i4);
        if (inser_data){



            Toast.makeText(MainActivity.this,"success",Toast.LENGTH_LONG).show();


        }
        else{
            Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
        }
    }
}




    /*private void getusers() {
        Call<List<users>> call = mapi.getusers();
        call.enqueue(new Callback<List<users>>() {
            @Override
            public void onResponse(Call<List<users>> call, Response<List<users>> response) {
                if(!response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<List<users>> call, Throwable t) {

            }
        });*/




