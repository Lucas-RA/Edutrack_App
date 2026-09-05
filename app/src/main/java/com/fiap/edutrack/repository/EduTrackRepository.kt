package com.fiap.edutrack.repository

import com.fiap.edutrack.data.mock.MockDataProvider
import com.fiap.edutrack.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositório único do MVP e **dono do estado vivo do app**.
 *
 * No projeto de referência do professor quem emite `Flow` é o DAO do Room. Aqui o
 * enunciado proíbe banco local, então o [MockDataProvider] ocupa o lugar do SQLite e é
 * este repositório que guarda o estado em memória com `MutableStateFlow`, expondo
 * `StateFlow` somente-leitura para fora. A cadeia fica
 * `MockDataProvider -> EduTrackRepository -> EduTrackViewModel`.
 *
 * Aqui não mora regra de negócio: o repositório só guarda e devolve estado. Quem decide
 * *quando* e *por que* o estado muda é o ViewModel.
 */
class EduTrackRepository(
    private val fonteDeDados: MockDataProvider
) {

    // ----- Estado vivo: muda durante o uso do app -----

    private val _missoesDaSemana = MutableStateFlow(fonteDeDados.missoesDaSemana)
    val missoesDaSemana: StateFlow<List<Missao>> = _missoesDaSemana.asStateFlow()

    private val _xpAtual = MutableStateFlow(fonteDeDados.alunoLogado.xpAtual)
    val xpAtual: StateFlow<Int> = _xpAtual.asStateFlow()

    private val _badges = MutableStateFlow(fonteDeDados.badges)
    val badges: StateFlow<List<Badge>> = _badges.asStateFlow()

    private val _certificados = MutableStateFlow(fonteDeDados.certificados)
    val certificados: StateFlow<List<Certificado>> = _certificados.asStateFlow()

    private val _aulas = MutableStateFlow(fonteDeDados.aulas)
    val aulas: StateFlow<List<Aula>> = _aulas.asStateFlow()

    private val _checkInsRecentes = MutableStateFlow(fonteDeDados.checkInsRecentes)
    val checkInsRecentes: StateFlow<List<CheckInRegistro>> = _checkInsRecentes.asStateFlow()

    // ----- Mutação: setters sem regra, chamados pelo ViewModel -----

    fun atualizarMissoes(missoes: List<Missao>) {
        _missoesDaSemana.value = missoes
    }

    fun definirXpAtual(xp: Int) {
        _xpAtual.value = xp
    }

    fun atualizarBadges(badges: List<Badge>) {
        _badges.value = badges
    }

    fun atualizarCertificados(certificados: List<Certificado>) {
        _certificados.value = certificados
    }

    fun atualizarAulas(aulas: List<Aula>) {
        _aulas.value = aulas
    }

    fun registrarCheckIn(registro: CheckInRegistro) {
        _checkInsRecentes.value = listOf(registro) + _checkInsRecentes.value
    }

    // ----- Catálogo estático: não muda em tempo de execução -----
    //
    // Estes dados são o catálogo do app — nada no MVP os altera. Envolvê-los em
    // `StateFlow` só por simetria acrescentaria cerimônia sem nenhum observador para
    // notificar, então continuam como lista direta.

    fun getInstitutos(): List<Instituto> = fonteDeDados.institutos

    fun getAluno(): Aluno = fonteDeDados.alunoLogado

    fun getPerguntasOnboarding(): List<PerguntaOnboarding> = fonteDeDados.perguntasOnboarding

    fun getTrilhas(): List<Trilha> = fonteDeDados.trilhas

    fun getTrilhaPorId(id: String): Trilha? = fonteDeDados.trilhas.find { it.id == id }

    fun getQuizzesPorTrilha(trilhaId: String): List<Quiz> =
        fonteDeDados.quizzes.filter { it.trilhaId == trilhaId }

    fun getEvolucaoVocacional(): List<EvolucaoVocacional> = fonteDeDados.evolucaoVocacional
}
