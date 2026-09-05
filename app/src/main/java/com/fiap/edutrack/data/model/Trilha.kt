package com.fiap.edutrack.data.model

enum class AreaTrilha(val rotulo: String) {
    LOGICA("Lógica"),
    STEM("STEM"),
    CARREIRA("Carreira"),
    SOFT_SKILLS("Soft skills")
}

data class Trilha(
    val id: String,
    val nome: String,
    val area: AreaTrilha,
    val moduloAtual: Int,
    val totalModulos: Int,
    val aulasRestantes: Int,
    val progresso: Float
)
