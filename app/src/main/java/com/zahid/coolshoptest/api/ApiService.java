package com.zahid.coolshoptest.api;


import com.zahid.coolshoptest.model.AccountModel;
import com.zahid.coolshoptest.model.SessionModel;
import com.zahid.coolshoptest.model.general.GeneralResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/coolshoptest/Session/new.php")
    @FormUrlEncoded
    Call<GeneralResponseModel<SessionModel>> login(@Field("email") String email,
                                                   @Field("password") String pass
    );

    @POST("/coolshoptest/users/avatar.php")
    @FormUrlEncoded
    Call<GeneralResponseModel<AccountModel>> postAvator(
            @Header("Authorization") String header,
            @Field("userid") String userID, @Field("avatar") String avatar
    );

    @GET("/coolshoptest/users/user.php")
    Call<GeneralResponseModel<AccountModel>> getUser(
            @Header("Authorization") String header,
            @Query("userid") String userID
    );

}
