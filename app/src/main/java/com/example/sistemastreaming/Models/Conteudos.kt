package com.example.sistemastreaming.Models
import com.google.gson.annotations.SerializedName

data class Conteudos (
    @SerializedName("conteudoId") val conteudoId: String,
    @SerializedName("playListId") val playListId: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("urlConteudo") val urlConteudo: String,
)