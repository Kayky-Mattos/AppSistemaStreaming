package com.example.sistemastreaming.Models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("senha") val senha: String,
    @SerializedName("salt") val salt: String,
    @SerializedName("playlist") val playlist: List<Playlist>,
)