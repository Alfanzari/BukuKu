package com.syauqialfanzari0008.bukuku.model

data class Buku(
    val id: String,
    val judul: String,
    val penulis: String,
    val deskripsi: String? = null,
    val imageId: String,
    val mine: String
)