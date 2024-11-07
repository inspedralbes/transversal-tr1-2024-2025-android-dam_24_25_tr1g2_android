package com.example.projecte12

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registrar : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var backToLoginButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar)

        emailEditText = findViewById(R.id.registerEmailEditText)
        passwordEditText = findViewById(R.id.registerPasswordEditText)
        registerButton = findViewById(R.id.registerSubmitButton)
        backToLoginButton = findViewById(R.id.backToLoginButton)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Configuración del botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateInput(email, password)) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, introduce un email y contraseña válidos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configuración del botón "Volver al Login"
        backToLoginButton.setOnClickListener {
            // Redirigir a la actividad de Login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Cierra la actividad de registro
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun registerUser(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()  // Aplicar los cambios

        Toast.makeText(this, "Registro exitoso.", Toast.LENGTH_LONG).show()
        finish() // Cierra la actividad de registro y regresa a Login
    }
}
