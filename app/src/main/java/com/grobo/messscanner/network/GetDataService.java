package com.grobo.messscanner.network;

import com.grobo.messscanner.database.MessModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    //users
    @GET("/mess/data/{mess}")
    Call<MessModel.MessSuper> getAllUsersOfThisMess(@Path("mess") int mess);

}