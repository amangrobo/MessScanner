package com.grobo.messscanner.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    //users
    @GET("/mess/get/{mess}")
    Call<ResponseBody> getAllUsersOfThisMess(@Path("mess") int mess);

    @GET("/mess/cancelled/{mess}")
    Call<ResponseBody> getCancelledData(@Path("mess") int mess);

}