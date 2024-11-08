package com.example.projecte12

import Producto
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CarritoActivity : AppCompatActivity() {

    private lateinit var carritoContainer: LinearLayout
    private lateinit var totalTextView: TextView
    private lateinit var botonAtras: Button
    private lateinit var botonComprar: Button
    private lateinit var productos: ArrayList<Producto>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        carritoContainer = findViewById(R.id.carritoContainer)
        totalTextView = findViewById(R.id.totalTextView)
        botonAtras = findViewById(R.id.botonAtras)
        botonComprar = findViewById(R.id.botonComprar)
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        productos = intent.getParcelableArrayListExtra<Producto>("carrito_productos") ?: ArrayList()

        if (productos.isNotEmpty()) {
            mostrarProductosEnCarrito(productos)
        } else {
            totalTextView.text = "Carrito vacío"
        }

        // Acción del botón "Atrás"
        botonAtras.setOnClickListener {
            val intent = Intent(this, Tienda::class.java)
            startActivity(intent)
            finish()
        }

        // Acción del botón "Comprar"
        botonComprar.setOnClickListener {
            if (productos.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            } else {
                procesarCompra(productos)
            }
        }
    }

    private fun mostrarProductosEnCarrito(productos: ArrayList<Producto>) {
        carritoContainer.removeAllViews()
        var total = 0.0

        for (producto in productos) {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.item_carrito_simple, carritoContainer, false)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val cantidadTextView: TextView = view.findViewById(R.id.cantidadTextView)
            val incrementButton: ImageButton = view.findViewById(R.id.incrementButton)
            val decrementButton: ImageButton = view.findViewById(R.id.decrementButton)
            val eliminarButton: ImageButton = view.findViewById(R.id.eliminarButton)

            productName.text = producto.producto
            productPrice.text = producto.precio
            cantidadTextView.text = producto.cantidad.toString()

            total += producto.precio.toDouble() * producto.cantidad

            incrementButton.setOnClickListener {
                producto.cantidad++
                cantidadTextView.text = producto.cantidad.toString()
                actualizarTotal()
            }

            decrementButton.setOnClickListener {
                if (producto.cantidad > 1) {
                    producto.cantidad--
                    cantidadTextView.text = producto.cantidad.toString()
                    actualizarTotal()
                } else {
                    Toast.makeText(this, "La cantidad no puede ser menor a 1", Toast.LENGTH_SHORT).show()
                }
            }

            eliminarButton.setOnClickListener {
                productos.remove(producto)
                mostrarProductosEnCarrito(productos)
            }

            carritoContainer.addView(view)
        }

        totalTextView.text = "Total: $total"
    }

    private fun actualizarTotal() {
        var total = 0.0
        for (producto in productos) {
            total += producto.precio.toDouble() * producto.cantidad
        }
        totalTextView.text = "Total: $total"
    }

    private fun procesarCompra(productos: ArrayList<Producto>) {
        val userId = sharedPreferences.getInt("userId", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        val detallesCompra = productos.joinToString(",") { "${it.producto} x ${it.cantidad}" }
        val totalCompra = productos.sumOf { it.precio.toDouble() * it.cantidad }
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(Date())

        // Validar que todos los campos estén completos
        if (detallesCompra.isEmpty() || totalCompra <= 0) {
            Toast.makeText(this, "Datos incompletos para realizar la compra.", Toast.LENGTH_SHORT).show()
            return
        }

        val pedido = Pedido(
            usuario_id = userId,
            detalles = detallesCompra,
            estado = "Rebut",  // El estado podría ser diferente según lo que indique tu API
            total = totalCompra,
            fecha_pedido = fechaFormateada
        )

        Log.d("Compra", "Pedido a enviar: $pedido")

        // Crear una lista de pedidos, aunque solo haya uno
        val listaDePedidos = listOf(pedido)

        // Realizar la llamada a la API con la lista de pedidos
        val call = RetroFit.api.registrarCompra(listaDePedidos)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CarritoActivity,
                        "Compra realizada con éxito.",
                        Toast.LENGTH_LONG
                    ).show()
                    productos.clear()
                    totalTextView.text = "Total: 0.0"
                    carritoContainer.removeAllViews()
                    finish()
                } else {
                    Log.e("ErrorCompra", "Error al registrar la compra: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@CarritoActivity,
                        "Error al registrar la compra.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ErrorConexion", "Error de conexión: ${t.message}")
                Toast.makeText(
                    this@CarritoActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}