package com.example.projecte12

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var guestLoginButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        guestLoginButton = findViewById(R.id.guestLoginButton)
        backButton = findViewById(R.id.backButton)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Tienda::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }

        guestLoginButton.setOnClickListener {
            Toast.makeText(this, "Entrando como invitado.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Tienda::class.java)
            intent.putExtra("guest", true)
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateLogin(email: String, password: String): Boolean {
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        return email == savedEmail && password == savedPassword
    }
}