package com.example.projecte12

import Producto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    // Endpoint para registrar un usuario
    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<ResponseBody>

    // Endpoint para login de un usuario
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/getProducto")
    fun getProductos(): Call<List<Producto>>

    @POST("/registrarCompra")
    fun registrarCompra(@Body listaDePedidos: List<Pedido>): Call<ResponseBody>
}
