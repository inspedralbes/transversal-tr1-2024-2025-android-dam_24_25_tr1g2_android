package com.example.projecte12

import Producto
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Date
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class CarritoActivity : AppCompatActivity() {

    private lateinit var carritoContainer: LinearLayout
    private lateinit var totalTextView: TextView
    private lateinit var botonAtras: Button
    private lateinit var botonComprar: Button

    private lateinit var productos: ArrayList<Producto> // Agregar variable para productos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        carritoContainer = findViewById(R.id.carritoContainer)
        totalTextView = findViewById(R.id.totalTextView)
        botonAtras = findViewById(R.id.botonAtras)
        botonComprar = findViewById(R.id.botonComprar)

        productos = intent.getParcelableArrayListExtra<Producto>("carrito_productos") ?: ArrayList()

        if (productos.isNotEmpty()) {
            mostrarProductosEnCarrito(productos)
        } else {
            totalTextView.text = "Carrito vacío"
        }

        // Acción del botón "Atrás"
        botonAtras.setOnClickListener {
            finish()  // Vuelve a la actividad anterior
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
        carritoContainer.removeAllViews() // Limpiar el contenedor antes de agregar productos
        var total = 0.0

        for (producto in productos) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_carrito_simple, carritoContainer, false)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val cantidadTextView: TextView = view.findViewById(R.id.cantidadTextView)
            val incrementButton: Button = view.findViewById(R.id.incrementButton)
            val decrementButton: Button = view.findViewById(R.id.decrementButton)
            val eliminarButton: Button = view.findViewById(R.id.eliminarButton)

            productName.text = producto.producto
            productPrice.text = producto.precio
            cantidadTextView.text = producto.cantidad.toString()

            // Calcular total con la cantidad
            total += producto.precio.toDouble() * producto.cantidad

            // Incrementar cantidad
            incrementButton.setOnClickListener {
                producto.cantidad++
                cantidadTextView.text = producto.cantidad.toString()
                actualizarTotal()
            }

            // Decrementar cantidad
            decrementButton.setOnClickListener {
                if (producto.cantidad > 1) {
                    producto.cantidad--
                    cantidadTextView.text = producto.cantidad.toString()
                    actualizarTotal()
                } else {
                    Toast.makeText(this, "La cantidad no puede ser menor a 1", Toast.LENGTH_SHORT).show()
                }
            }

            // Eliminar producto
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
        // Genera los detalles de la compra en formato JSON o String
        val detallesCompra = productos.joinToString(",") { "${it.producto} x ${it.cantidad}" }
        val totalCompra = productos.sumOf { it.precio.toDouble() * it.cantidad }

        // Crear el objeto pedido para enviar al servidor
        val pedido = Pedidios(
            id = 0,  // El ID se autogenerará en la BD
            usuario_id = 1,  // Supón que tienes un ID de usuario (puedes cambiar este valor según la sesión del usuario)
            detalles = detallesCompra,
            total = totalCompra.toFloat(),
            fecha = Date()
        )

        // Envuelve el objeto pedido en una lista
        val listaDePedidos = listOf(pedido)

        // Llamar a la API para registrar la compra
        val call = RetroFit.api.registrarCompra(listaDePedidos)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CarritoActivity, "Compra realizada con éxito.", Toast.LENGTH_LONG).show()
                    productos.clear()
                    totalTextView.text = "Total: 0.0"
                    carritoContainer.removeAllViews()
                    finish() // Cerrar la actividad después de la compra
                } else {
                    Toast.makeText(this@CarritoActivity, "Error al registrar la compra.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@CarritoActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}