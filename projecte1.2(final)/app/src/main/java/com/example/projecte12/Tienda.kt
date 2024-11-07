package com.example.projecte12

import Producto
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
    private lateinit var searchEditText: EditText
    private val carrito = Carrito()
    private lateinit var userInfoTextView: TextView
    private lateinit var loginButtonInStore: Button
    private lateinit var registerButtonInStore: Button
    private lateinit var viewUserInfoButton: Button // Nuevo botón para ver información del usuario
    private lateinit var skipLoginButton: Button // Botón para entrar sin iniciar sesión
    private lateinit var cartIcon: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tienda)


        // Inicializa las vistas
        productosContainer = findViewById(R.id.productosContainer)
        botonCarrito = findViewById(R.id.botonCarrito)
        searchEditText = findViewById(R.id.searchEditText)
        userInfoTextView = findViewById(R.id.userInfoTextView)
        loginButtonInStore = findViewById(R.id.loginButtonInStore)
        registerButtonInStore = findViewById(R.id.registerButtonInStore)
        viewUserInfoButton = findViewById(R.id.viewUserInfoButton)
        skipLoginButton = findViewById(R.id.skipLoginButton)
        cartIcon = findViewById(R.id.cartIcon)

        fetchProductos()
        cartIcon.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putParcelableArrayListExtra("carrito_productos", ArrayList(carrito.obtenerProductos()))
            startActivity(intent)
        }


        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)
        val userPassword = sharedPreferences.getString("user_password", null)

        val isGuest = intent.getBooleanExtra("guest", false)

        if (isGuest) {
            // Configuración para el modo invitado
            userInfoTextView.text = "Usuario: Invitado"
            loginButtonInStore.visibility = Button.GONE
            registerButtonInStore.visibility = Button.GONE
            skipLoginButton.visibility = Button.GONE
            viewUserInfoButton.visibility = Button.GONE
        } else if (userEmail != null) {
            // Configuración para el usuario registrado
            userInfoTextView.text = "Usuario: $userEmail"
            loginButtonInStore.visibility = Button.GONE
            registerButtonInStore.visibility = Button.GONE
            viewUserInfoButton.visibility = Button.VISIBLE
            viewUserInfoButton.setOnClickListener {
                showUserInfo(userEmail, userPassword)
            }
            skipLoginButton.visibility = Button.GONE
        } else {
            // Configuración cuando no se ha iniciado sesión
            userInfoTextView.text = "No has iniciado sesión"
            loginButtonInStore.visibility = Button.VISIBLE
            registerButtonInStore.visibility = Button.VISIBLE
            skipLoginButton.visibility = Button.VISIBLE
            loginButtonInStore.setOnClickListener {
                startActivity(Intent(this, Login::class.java))
            }
            registerButtonInStore.setOnClickListener {
                startActivity(Intent(this, Registrar::class.java))
            }
            skipLoginButton.setOnClickListener {
                // Lógica para entrar como invitado
                Toast.makeText(this, "Entrando como invitado.", Toast.LENGTH_SHORT).show()
                userInfoTextView.text = "Usuario: Invitado"
                loginButtonInStore.visibility = Button.GONE
                registerButtonInStore.visibility = Button.GONE
                skipLoginButton.visibility = Button.GONE
                viewUserInfoButton.visibility = Button.GONE
            }
        }

        // Botón carrito
        botonCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            intent.putParcelableArrayListExtra("carrito_productos", ArrayList(carrito.obtenerProductos()))
            startActivity(intent)
        }

        // Filtrar productos
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProductos(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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

    private fun displayProductos(productos: List<Producto>) {
        productosContainer.removeAllViews()
        for (producto in productos) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_product_simple, productosContainer, false)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val productImage: ImageView = view.findViewById(R.id.productImagen)
            val addToCartButton: Button = view.findViewById(R.id.addToCartButton)

            productName.text = producto.producto
            productPrice.text = producto.precio

            // Aquí se carga la imagen utilizando Glide y la URL construida
            val imageUrl = "http://10.0.2.2:3001/imagen/${producto.imagen}"
            Glide.with(this)
                .load(imageUrl)
                .into(productImage)

            addToCartButton.setOnClickListener {
                carrito.agregarProducto(producto)
                showToast("${producto.producto} agregado al carrito.")
            }

            productosContainer.addView(view)
        }
    }

    private fun filterProductos(query: String) {
        val filteredProductos = productos.filter { it.producto.contains(query, ignoreCase = true) }
        displayProductos(filteredProductos)
    }

    private fun showUserInfo(email: String?, password: String?) {
        Toast.makeText(this, "Email: $email\nContraseña: $password", Toast.LENGTH_LONG).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



