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

    private lateinit var nombreEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var comfirmarContraseñaEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar)

        nombreEditText = findViewById(R.id.registerNameEditText)
        emailEditText = findViewById(R.id.registerEmailEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        comfirmarContraseñaEditText = findViewById(R.id.registerConfirmPasswordEditText)
        direccionEditText = findViewById(R.id.direccioEditText)
        registerButton = findViewById(R.id.registerSubmitButton)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Configuración del botón de registro
        registerButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val direccion = direccionEditText.text.toString()

            println("Email: $email")
            println("Password: $password")

            if (validateInput(nombre, email, password, direccion)) {
                registerUser(nombre, email, password, direccion)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(nombre: String, email: String, password: String, direccion: String): Boolean {
        return nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && direccion.isNotEmpty()
    }

    private fun registerUser(nombre: String, email: String, password: String, direccion: String) {
        val registerRequest = RegisterRequest(nombre, email, password, direccion)

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