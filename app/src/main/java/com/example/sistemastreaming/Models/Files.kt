package com.example.sistemastreaming.Models

import com.google.gson.annotations.SerializedName

data class Files (
    @SerializedName("fileId") val fileId: String,
    @SerializedName("criadorId") val criadorId: String,
    @SerializedName("criadorName") val criadorName: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("banner") val banner: String,
    @SerializedName("fileUrl") val fileUrl: String
)