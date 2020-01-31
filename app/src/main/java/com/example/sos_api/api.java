package com.example.sos_api;

import java.lang.ref.SoftReference;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface api {
    @FormUrlEncoded
    @POST("auth/signup")//endpoint

    Call<ResponseBody> create_user( //response we shall get
                                @Field("name") String name,
                                @Field("email")String email,
                                @Field("password") String password,
                                @Field("phone") String phone,
                                @Field("address") String address


            );

    @FormUrlEncoded
    @POST("auth/login")
    Call<login_response> userlogin(
            @Field("email")String email,
            @Field("password")String password

    );
    @GET("users/getone") //endpoint of api url
    Call<login_response> getuser(@Query("id") String userId);
    @FormUrlEncoded
    @POST("loc/postlatlong")
    Call<login_response> postloc(
            @Field("uid") String userid,
            @Field("lat") String lat,
            @Field("long") String longi
    );

    @GET("loc/getone")
    Call<loc_response> getloc(@Query("uid") String userid);

    @GET("users/getsos")
    Call<loc_response> getuserloc(
            @Query("uid") String uid,
            @Query("lat") String lat,
            @Query("long") String longi,
            @Query("max") String maxx,
            @Query("level") String level
    );
    @GET("loc/nearby")
    Call<nearbylist> getnear(@Query("lat")String lat,
                               @Query("long")String longi
    );



}
