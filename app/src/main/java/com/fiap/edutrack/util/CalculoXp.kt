package com.fiap.edutrack.util

/**
 * Regras aritméticas de XP e nível. Puras e testáveis isoladamente — a mesma conta
 * aparecia na Home, em Conquistas e no ViewModel.
 */

/** Progresso até o próximo nível, sempre no intervalo 0f..1f. */
fun progressoDeXp(xpAtual: Int, xpProximoNivel: Int): Float =
    if (xpProximoNivel <= 0) 0f else (xpAtual.toFloat() / xpProximoNivel).coerceIn(0f, 1f)

/** Quanto ainda falta para o próximo nível. Nunca negativo. */
fun xpRestante(xpAtual: Int, xpProximoNivel: Int): Int =
    (xpProximoNivel - xpAtual).coerceAtLeast(0)
