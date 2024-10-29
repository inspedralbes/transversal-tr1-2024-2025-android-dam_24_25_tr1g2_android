package com.example.projecte12

import Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/getProducto")
    fun getProductos(): Call<List<Producto>>

    @POST("/registrarCompra")
    fun registrarCompra(@Body pedidos: List<Pedidios>): Call<Unit>

    @POST("/getLogin")
    fun login(@Body loginRequest: RegisterRequest): Call<Void>

    @POST("/getLogin")
    fun register(@Body registerRequest: RegisterRequest): Call<Void>
}