package com.example.projecte12

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tienda : AppCompatActivity() {

    var productos: List<Producto> = emptyList()
    private lateinit var productosContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tienda)
        productosContainer = findViewById(R.id.productosContainer)
        fetchProductos()
    }

    private fun fetchProductos() {
        val call = RetroFit.api.getProductos()
        call.enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        productos = responseBody
                        Log.d("Tienda", "Respuesta: $responseBody")

                        if (productos.isNotEmpty()) {
                            displayProductos(productos)
                        } else {
                            showToast("No hay productos disponibles.")
                        }
                    } else {
                        showToast("Error al recibir los productos.")
                    }
                } else {
                    showToast("Error al obtener los productos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                showToast("Error de conexión: ${t.localizedMessage}")
                Log.e("Tienda", "Fallo en la conexión: ${t.localizedMessage}")
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun displayProductos(productos: List<Producto>) {
        for (producto in productos) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_product_simple, productosContainer, false)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val productImage: ImageView = view.findViewById(R.id.productImagen)

            productName.text = producto.producto
            productPrice.text = producto.precio

            Glide.with(this)
                .load(producto.imagen)
                .into(productImage)

            productosContainer.addView(view)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}