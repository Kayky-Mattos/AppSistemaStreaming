package com.example.sistemastreaming.Models
import com.google.gson.annotations.SerializedName

data class PostUser(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String,
)