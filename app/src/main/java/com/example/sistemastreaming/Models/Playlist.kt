package com.example.sistemastreaming.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Playlist (
    @SerializedName("playListId") val playlistId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("conteudos") var conteudos: List<Conteudos>
    ): Serializable