package com.fiap.edutrack.navigation

/**
 * Nomes de rota do NavHost. Centralizados aqui para evitar strings soltas
 * espalhadas pelas telas.
 */
object Rotas {
    const val LOGIN = "login"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val CONTEUDO = "conteudo"
    const val QUIZ = "quiz/{trilhaId}"
    const val DETALHE_AULA = "aula/{aulaId}"
    const val CONQUISTAS = "conquistas"
    const val CHECKIN = "checkin"
    const val PERFIL = "perfil"

    fun quizComTrilha(trilhaId: String) = "quiz/$trilhaId"

    fun aulaComId(aulaId: String) = "aula/$aulaId"
}
