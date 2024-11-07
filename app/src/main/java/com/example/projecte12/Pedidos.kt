package com.example.projecte12

data class Pedido(
    val usuario_id: Int,
    val detalles: String,
    val estado: String,
    val total: Double,
    val fecha_pedido: String
)