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
        // Aquí puedes manejar el proceso de compra (guardar en base de datos, enviar a servidor, etc.)
        Toast.makeText(this, "Compra realizada con éxito.", Toast.LENGTH_LONG).show()

        // Opción: Vaciar el carrito y volver a la pantalla de la tienda
        productos.clear()
        totalTextView.text = "Total: 0.0"
        carritoContainer.removeAllViews()

        finish()  // Cerrar la actividad después de la compra
    }
}
