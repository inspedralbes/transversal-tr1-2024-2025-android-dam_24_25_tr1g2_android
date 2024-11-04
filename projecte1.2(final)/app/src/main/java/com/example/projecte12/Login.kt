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

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var guestLoginButton: Button // Nuevo botón para entrar como invitado

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        guestLoginButton = findViewById(R.id.guestLoginButton) // Inicializar el nuevo botón

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Configurar botón de iniciar sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Tienda::class.java) // Inicia Tienda
                startActivity(intent)
                finish() // Opcional: cerrar LoginActivity
            } else {
                Toast.makeText(this, "Email o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
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
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)

        return email == savedEmail && password == savedPassword
    }
}
