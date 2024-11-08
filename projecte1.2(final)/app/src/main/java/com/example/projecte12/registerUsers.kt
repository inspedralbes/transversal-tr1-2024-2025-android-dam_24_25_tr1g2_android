package com.example.projecte12

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val direccion: String
)

data class LoginRequest(
    val email: String,
    val password: String
)