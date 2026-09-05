package com.fiap.edutrack.data.model

enum class Humor(val emoji: String, val rotulo: String) {
    MAL("😟", "Mal"),
    TRISTE("😕", "Triste"),
    NORMAL("😐", "Normal"),
    BEM("🙂", "Bem"),
    OTIMO("😄", "Ótimo")
}

enum class CargaTarefas(val rotulo: String, val descricao: String) {
    TRANQUILO("Tranquilo(a)", "Consigo dar conta"),
    ALGUMAS_PESAM("Um pouco", "Algumas tarefas pesam"),
    SOBRECARREGADO("Sobrecarregado(a)", "Sinto que não dou conta")
}

data class CheckInRegistro(
    val data: String,
    val humor: Humor,
    val cargaTarefas: CargaTarefas,
    val comentario: String
)
