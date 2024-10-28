package com.example.projecte12

import Producto

class Carrito {
    private val productos: MutableList<Producto> = mutableListOf()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    fun obtenerProductos(): List<Producto> {
        return productos
    }

    fun calcularPrecioTotal(): Double {
        return productos.sumOf { it.precio.toDouble() }
    }

    fun vaciarCarrito() {
        productos.clear()
    }
}
