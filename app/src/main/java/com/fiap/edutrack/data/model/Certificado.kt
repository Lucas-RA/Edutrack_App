package com.fiap.edutrack.data.model

data class Certificado(
    val id: String,
    val nome: String,
    val instituto: String,
    val cargaHoraria: Int,
    val dataEmissao: String,
    val publicado: Boolean
)
