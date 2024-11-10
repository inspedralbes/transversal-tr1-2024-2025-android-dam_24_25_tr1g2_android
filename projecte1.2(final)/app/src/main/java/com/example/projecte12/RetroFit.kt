package com.example.projecte12

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.projecte12.Api  // Importar tu propia interfaz Api

object RetroFit {
    private const val BASE_URL = "http://10.0.2.2:3001"  // Cambia esta URL por la tuya si es necesario

    // Crear un interceptor de logging para ver las solicitudes y respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Crear un cliente OkHttp con el interceptor de logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // Añadir el interceptor de logging
        .build()

    // Crear un Gson con modo lenient
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // Crear Retrofit con el cliente OkHttp y Gson configurados
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)  // Añadir el cliente OkHttp con el interceptor
        .addConverterFactory(GsonConverterFactory.create(gson))  // Usar Gson con modo lenient
        .build()

    // Crear una instancia de la API
    val api: Api = retrofit.create(Api::class.java)  // Usar tu interfaz Api
}