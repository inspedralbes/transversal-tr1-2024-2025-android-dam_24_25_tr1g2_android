package com.example.projecte12


import Producto
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
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

    private var productos: List<Producto> = emptyList()
    private lateinit var productosContainer: LinearLayout
    private lateinit var botonCarrito: Button
    private val carrito = Carrito()  // Instancia del carrito

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tienda)
        productosContainer = findViewById(R.id.productosContainer)
        botonCarrito = findViewById(R.id.botonCarrito)

        fetchProductos()

        botonCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putParcelableArrayListExtra("carrito_productos", ArrayList(carrito.obtenerProductos()))
            startActivity(intent)
        }

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

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                showToast("Error de conexión: ${t.message}")
                Log.e("Tienda", "Fallo en la conexión: ${t.message}")
                finish()
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
            val addToCartButton: Button = view.findViewById(R.id.addToCartButton)

            productName.text = producto.producto
            productPrice.text = producto.precio

            Glide.with(this)
                .load(producto.imagen)
                .into(productImage)

            // Lógica para agregar al carrito
            addToCartButton.setOnClickListener {
                carrito.agregarProducto(producto)
                showToast("${producto.producto} agregado al carrito.")
            }

            productosContainer.addView(view)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

