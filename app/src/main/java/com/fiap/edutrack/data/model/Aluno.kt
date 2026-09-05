package com.fiap.edutrack.data.model

data class Aluno(
    val nome: String,
    val email: String,
    val matricula: String,
    val instituto: Instituto,
    val anoEgresso: Int,
    val nivel: Int,
    val xpAtual: Int,
    val xpProximoNivel: Int,
    val streakDias: Int,
    val tituloNivel: String
)
