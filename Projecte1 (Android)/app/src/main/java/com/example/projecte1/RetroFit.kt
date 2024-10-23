package com.example.projecte1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetroFit {
    private const val BASE_URL = "http://localhost:3000"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}