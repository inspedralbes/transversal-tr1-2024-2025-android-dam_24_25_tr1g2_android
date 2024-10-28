package com.example.projecte12

import Producto
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CarritoActivity : AppCompatActivity() {

    private lateinit var carritoContainer: LinearLayout
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        carritoContainer = findViewById(R.id.carritoContainer)
        totalTextView = findViewById(R.id.totalTextView)

        val productos = intent.getParcelableArrayListExtra<Producto>("carrito_productos")

        if (productos != null) {
            mostrarProductosEnCarrito(productos)
        } else {
            totalTextView.text = "Carrito vacío"
        }
    }

    private fun mostrarProductosEnCarrito(productos: ArrayList<Producto>) {
        var total = 0.0

        for (producto in productos) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_carrito_simple, carritoContainer, false)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)


            productName.text = producto.producto
            productPrice.text = producto.precio

            carritoContainer.addView(view)

            total += producto.precio.toDouble() // Asegúrate de que el precio sea un número
        }

        totalTextView.text = "Total: $total"
    }
}

