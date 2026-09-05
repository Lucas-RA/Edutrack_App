package com.fiap.edutrack.data.model

data class Aula(
    val id: String,
    val titulo: String,
    val instituto: String,
    val horario: String,
    val data: String,
    val descricao: String,
    val topicos: List<String>,
    val presencaConfirmada: Boolean
)
