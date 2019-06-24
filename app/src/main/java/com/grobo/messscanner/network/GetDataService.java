package com.grobo.messscanner.network;

import com.grobo.messscanner.database.UserModel;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface GetDataService {

    //users
    @GET("/users")
    Call<List<UserModel>> getAllUsersOfThisMess(@Body RequestBody body);

    @GET("/users/cancelled")
    Call<List<UserModel>> login(@Body RequestBody body);

}