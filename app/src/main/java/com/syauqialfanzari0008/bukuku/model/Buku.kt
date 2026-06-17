package com.syauqialfanzari0008.bukuku.model

import com.squareup.moshi.Json

data class Buku(
    val id: String,
    val judul: String,
    val penulis: String,
    val deskripsi: String? = null,
    val imageId: String,
    val mine: String,
    @Json(name = "is_favorite") val isFavorite: Int = 0
)