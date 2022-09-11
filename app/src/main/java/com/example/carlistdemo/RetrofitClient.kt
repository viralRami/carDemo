package com.example.carlistdemo


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient private constructor() {
    private val myApi: Api
    fun getMyApi(): Api {
        return myApi
    }

    companion object {
        @get:Synchronized
        var instance: RetrofitClient? = null
            get() {
                if (field == null) {
                    field = RetrofitClient()
                }
                return field
            }
            private set
    }

    init {
        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl("https://myfakeapi.com/api/cars/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        myApi =
            retrofit.create<Api>(Api::class.java)
    }
}