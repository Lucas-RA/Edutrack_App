package com.fiap.edutrack.data.model

/**
 * Tipo da missão. Existe para o ViewModel encontrar a missão certa pelo **que ela é**,
 * e não por um id literal: `find { it.id == "missao-02" }` quebra em silêncio quando o
 * mock muda, e deixava a missão de check-in sem nunca progredir.
 */
enum class TipoMissao {
    QUIZ,
    CHECKIN,
    AULA,
    FORUM
}

data class Missao(
    val id: String,
    val titulo: String,
    val tipo: TipoMissao,
    val xp: Int,
    val progressoAtual: Int,
    val progressoTotal: Int,
    val concluida: Boolean
)
