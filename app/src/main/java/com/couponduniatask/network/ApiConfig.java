package com.couponduniatask.network;


import com.couponduniatask.network.Response.GetResponse;

import retrofit.Callback;
import retrofit.http.GET;

public interface ApiConfig {


    @GET("/task.txt")
    public void getdata(Callback<GetResponse.ApiResponse> genericResponseCallback);
}
