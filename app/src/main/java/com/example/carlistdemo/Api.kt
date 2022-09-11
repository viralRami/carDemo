package com.example.carlistdemo

import retrofit2.Call
import retrofit2.http.GET




interface Api {

    @GET(".")
    fun getCar(): Call<Cars>
}