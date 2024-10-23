package com.example.projecte1

import retrofit2.Call
import retrofit2.http.POST
interface ApiService {
    @POST("/getProducto")
    fun getProductos() : Call<Productos>

    //@POST("/finalista")
    //fun submitAnswers(@Body request: FinalAnswersRequest): Call<FinalAnswersResponse>
    
}