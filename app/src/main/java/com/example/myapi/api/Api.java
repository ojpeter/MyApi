package com.example.myapi.api;

import com.example.myapi.models.DefaultResponse;
import com.example.myapi.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("uname") String uname,
            @Field("uphone") String uphone,
            @Field("upassword") String upassword,
            @Field("uposition") String uposition,
            @Field("ucreated") String ucreated
    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userLogin(
            @Field("uphone") String uphone,
            @Field("upassword") String upassword
    );

}
