package com.example.projecte12

import Producto

class Carrito {
    private val productos: MutableList<Producto> = mutableListOf()

    fun agregarProducto(producto: Producto) {
        val existente = productos.find { it.id == producto.id }
        if (existente != null) {
            existente.cantidad++
        } else {
            productos.add(producto)
        }
    }

    fun obtenerProductos(): List<Producto> = productos

    fun calcularPrecioTotal(): Double = productos.sumOf { it.precio.toDouble() * it.cantidad }

    fun vaciarCarrito() {
        productos.clear()
    }
}
