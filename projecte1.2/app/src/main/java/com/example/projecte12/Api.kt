package com.example.projecte12

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/getProductos")
    fun getProductos(): Call<List<Producto>>
}