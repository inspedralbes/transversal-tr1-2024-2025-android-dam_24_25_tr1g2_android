package com.example.projecte12

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var editProfileButton: Button
    private lateinit var loginButton: Button // Añadimos el botón para ir al login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profileactivity)

        // Inicializamos las vistas
        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        editProfileButton = findViewById(R.id.editProfileButton)
        loginButton = findViewById(R.id.loginButton) // Inicializamos el nuevo botón

        // Recibir datos del Intent
        val userEmail = intent.getStringExtra("user_email")
        val userName = intent.getStringExtra("user_name") // Asumiendo que también se pasa el nombre

        // Llenar la información del perfil
        profileEmail.text = userEmail
        profileName.text = userName ?: "Usuario Anónimo"

        // Usar Glide para cargar la imagen del perfil (puedes cambiarla con tu propia URL o drawable)
        Glide.with(this)
            .load("https://example.com/path/to/profile/image.jpg") // Aquí va tu URL de imagen
            .into(profileImage)

        // Acción para el botón de retroceder
        editProfileButton.setOnClickListener {
            // Ejecutar el comportamiento de retroceso
            onBackPressed() // Esto hará que la actividad actual se cierre y regrese a la anterior
        }

        // Acción para el botón de ir a Login
        loginButton.setOnClickListener {
            // Abrir LoginActivity cuando se haga clic en el botón
            val intent = Intent(this, Login::class.java) // Cambia "LoginActivity" al nombre de tu actividad de login
            startActivity(intent)
        }
    }
}
