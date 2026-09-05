package com.fiap.edutrack.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Rótulo de data no formato que os check-ins mockados já usam ("Quarta, 21 mai").
 *
 * Recebe a data em vez de ler o relógio: assim continua sendo função pura e pode ser
 * verificada com uma data fixa. `SimpleDateFormat` em vez de `java.time` porque o
 * `minSdk` do projeto é 24 e não há desugaring configurado.
 */
fun formatarDataDeCheckIn(data: Date, locale: Locale = Locale("pt", "BR")): String {
    val diaDaSemana = SimpleDateFormat("EEEE", locale).format(data)
        .replaceFirstChar { it.uppercase() }
        .substringBefore("-")
    val diaEMes = SimpleDateFormat("d MMM", locale).format(data).replace(".", "")
    return "$diaDaSemana, $diaEMes"
}
