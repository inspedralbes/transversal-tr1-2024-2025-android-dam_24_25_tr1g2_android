package com.example.projecte12

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFit {
    const val BASE_URL = "http://10.0.2.2:3000"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: Api = retrofit.create(Api::class.java)
}