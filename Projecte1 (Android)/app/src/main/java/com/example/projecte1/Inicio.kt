package com.example.projecte1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Inicio(intent: Intent) : AppCompatActivity() {
    private  lateinit var StartButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StartButton = findViewById(R.id.tienda)

        StartButton.setOnClickListener{
            val intent = Intent(this, Tienda::class.java)
            Inicio(intent)
        }
    }
}