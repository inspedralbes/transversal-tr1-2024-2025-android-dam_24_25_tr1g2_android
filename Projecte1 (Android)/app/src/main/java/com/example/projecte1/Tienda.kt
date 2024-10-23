package com.example.projecte1

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide

class Tienda : AppCompatActivity() {

    private lateinit var productosContainer: LinearLayout

    private var productos: List<Producto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tienda)
        productosContainer = findViewById(R.id.productosContainer)

        fechPreoductos()
    }

    private fun fechPreoductos(){
        val call = RetroFit.apiService.getProductos()
         call.enqueue(object : Callback<Productos>{
            override fun onResponse(call: Call<Productos>, response: Response<Productos>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        productos = responseBody.productos ?: emptyList()

                        if (productos.isNotEmpty()) {
                            displayProductos(productos)
                        } else {
                            showToast("No hay productos disponibles.")
                            finish()
                        }
                    } else {
                        showToast("Error al recibir los productos.")
                        finish()
                    }
                } else {
                    showToast("Error al obtener los productos: ${response.code()}")
                    finish()
                }
            }

            override fun onFailure(call: Call<Productos>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                finish()
            }
        })
    }

    private fun displayProductos(productos: List<Producto>) {
        productosContainer.removeAllViews()

        for (producto in productos) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_product_simple, productosContainer, false)

            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val productImage: ImageView = view.findViewById(R.id.productImage)

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
