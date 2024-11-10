package com.example.projecte12

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var guestLoginButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        guestLoginButton = findViewById(R.id.guestLoginButton)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        // Configurar botón de iniciar sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                val loginRequest = LoginRequest(email, password)

                // Cambiar el tipo de callback a LoginResponse
                RetroFit.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                // Guardar el userId en SharedPreferences
                                val editor = sharedPreferences.edit()
                                editor.putInt("userId", loginResponse.userId)
                                editor.apply()

                                Toast.makeText(this@Login, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Login, Tienda::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@Login, "Error en la respuesta del servidor: No se recibió el ID del usuario.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Aquí podemos imprimir el cuerpo de la respuesta para ver qué está llegando
                            val errorBody = response.errorBody()?.string() // Obtener el cuerpo de error
                            Log.e("LoginError", "Error en la conexión: ${response.message()}, Cuerpo: $errorBody")
                            Toast.makeText(this@Login, "Error en el inicio de sesión: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }


                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // Mostrar detalles de la excepción en caso de fallo en la conexión
                        Toast.makeText(this@Login, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botón de registro
        registerTextView.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }

        // Configurar botón para entrar como invitado
        guestLoginButton.setOnClickListener {
            Toast.makeText(this, "Entrando como invitado.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Tienda::class.java)
            intent.putExtra("guest", true) // Extra para indicar modo invitado
            startActivity(intent)
            finish()
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}