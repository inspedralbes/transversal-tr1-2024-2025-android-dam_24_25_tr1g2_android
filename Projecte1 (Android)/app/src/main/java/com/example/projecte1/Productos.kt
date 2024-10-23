package com.example.projecte1

data class Productos(
    val productos: List<Producto>?
)

data class Producto(
    val id: Int,
    val imagen: String,
    val producto: String,
    val precio: String
)