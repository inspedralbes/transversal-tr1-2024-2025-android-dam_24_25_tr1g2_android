package com.example.projecte12

import java.util.Date

data class Pedidios(
    val id: Int,
    val usuario_id: Int,
    val detalles: String,
    val total: Float,
    val fecha: Date
)