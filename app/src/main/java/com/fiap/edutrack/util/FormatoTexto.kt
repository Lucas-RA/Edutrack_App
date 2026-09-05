package com.fiap.edutrack.util

import com.fiap.edutrack.data.model.Missao
import java.util.Locale

/**
 * Funções puras de formatação de texto — sem Compose, sem estado, sem Android.
 * Ficam aqui para não serem reescritas em cada tela que precisa do mesmo rótulo.
 */

/** Iniciais para o avatar: "Lucas Rodrigues" -> "LR". Usada por `EduTrackAvatar`. */
fun iniciaisDoNome(nome: String): String =
    nome.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

/** Rótulo de progresso de missão, repetido na Home e em Conquistas. */
fun rotuloProgressoMissao(missao: Missao): String =
    if (missao.concluida) {
        "Concluída"
    } else {
        "${missao.progressoAtual} de ${missao.progressoTotal} concluídos"
    }

/** XP com separador de milhar no padrão brasileiro: 1240 -> "1.240". */
fun formatarXp(valor: Int, locale: Locale = Locale("pt", "BR")): String =
    String.format(locale, "%,d", valor)
