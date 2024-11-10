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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var guestLoginButton: Button // Nuevo botón para entrar como invitado

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Asegúrate de que tu XML sigue siendo el mismo

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)
        guestLoginButton = findViewById(R.id.guestLoginButton) // Inicializar el nuevo botón

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        // Configurar botón de iniciar sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                val loginRequest = LoginRequest(email, password)

                // Realizar la solicitud de login usando Retrofit
                RetroFit.api.login(loginRequest).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Login, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()

                            // Supongamos que la respuesta contiene el nombre del usuario y el correo
                            val userEmail = email // Aquí usas el email que el usuario ha introducido
                            val userName = "Nombre Real" // Aquí deberías obtener el nombre real desde la respuesta del servidor

                            // Enviar los datos de usuario al siguiente Intent
                            val intent = Intent(this@Login, Tienda::class.java)
                            intent.putExtra("user_email", userEmail)
                            intent.putExtra("user_name", userName) // Enviar el nombre real del usuario
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login, "Email o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@Login, "Error en la conexión.", Toast.LENGTH_SHORT).show()
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
            intent.putExtra("guest", true) // Pasar el extra para indicar modo invitado
            startActivity(intent)
            finish()
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
        // Validar que los campos de email y contraseña no estén vacíos
        return email.isNotEmpty() && password.isNotEmpty()
    }
}
