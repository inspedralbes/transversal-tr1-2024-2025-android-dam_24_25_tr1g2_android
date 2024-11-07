package com.example.projecte12

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody


class Registrar : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar)

        emailEditText = findViewById(R.id.registerEmailEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        registerButton = findViewById(R.id.registerSubmitButton)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Configuración del botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            println("Email: $email")
            println("Password: $password")

            if (validateInput(email, password)) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, introduce un email y contraseña válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun registerUser(email: String, password: String) {
        val registerRequest = RegisterRequest(email, password)

        // Llamada Retrofit para registrar el usuario
        RetroFit.api.register(registerRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Registrar, "Registro exitoso.", Toast.LENGTH_LONG).show()
                    finish() // Cierra la actividad de registro y regresa a Login
                } else {
                    println(call.request())
                    Toast.makeText(this@Registrar, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Registrar, "Error en la conexión.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}