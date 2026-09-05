package com.fiap.edutrack.data.model

data class Quiz(
    val id: String,
    val trilhaId: String,
    val pergunta: String,
    val alternativas: List<String>,
    val indiceCorreto: Int,
    val xp: Int
)
