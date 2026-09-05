package com.fiap.edutrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.edutrack.data.model.*
import com.fiap.edutrack.repository.EduTrackRepository
import com.fiap.edutrack.util.formatarDataDeCheckIn
import com.fiap.edutrack.util.progressoDeXp
import com.fiap.edutrack.util.xpRestante
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date

/**
 * ViewModel único do app, como o `TarefaViewModel` do projeto de referência.
 * Compartilhado entre as telas pelo grafo do NavHost e construído pela
 * [EduTrackViewModelFactory] — o repositório é obrigatório no construtor, de propósito:
 * a cadeia de criação precisa ficar visível em um lugar só.
 *
 * Fluxo unidirecional: a tela chama uma função pública daqui, o estado desce de volta
 * por `StateFlow`. Nenhuma tela altera estado por conta própria.
 */
class EduTrackViewModel(
    private val repository: EduTrackRepository
) : ViewModel() {

    /** Janela padrão de `stateIn` — mantém o estado vivo em recomposições e rotação. */
    private val enquantoObservado = SharingStarted.WhileSubscribed(5_000)

    // ----- Retorno visual: eventos de uma vez só, consumidos pelo Snackbar do NavHost -----
    private val _mensagens = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val mensagens: SharedFlow<String> = _mensagens.asSharedFlow()

    private fun avisar(texto: String) {
        _mensagens.tryEmit(texto)
    }

    // ----- Catálogo estático -----
    val institutos: List<Instituto> = repository.getInstitutos()
    val aluno: Aluno = repository.getAluno()
    val perguntasOnboarding: List<PerguntaOnboarding> = repository.getPerguntasOnboarding()
    val trilhas: List<Trilha> = repository.getTrilhas()
    val evolucaoVocacional: List<EvolucaoVocacional> = repository.getEvolucaoVocacional()

    fun trilhaPorId(id: String): Trilha? = repository.getTrilhaPorId(id)

    /**
     * A trilha do "continuar de onde parou": a de maior progresso entre as que ainda não
     * terminaram. Se todas estiverem concluídas, não há o que continuar.
     */
    val trilhaEmDestaque: Trilha? =
        trilhas.filter { it.progresso < 1f }.maxByOrNull { it.progresso }

    fun quizzesDaTrilha(trilhaId: String): List<Quiz> = repository.getQuizzesPorTrilha(trilhaId)

    // ----- Login -----
    private val _institutoSelecionado = MutableStateFlow<Instituto?>(null)
    val institutoSelecionado: StateFlow<Instituto?> = _institutoSelecionado.asStateFlow()

    fun selecionarInstituto(instituto: Instituto) {
        _institutoSelecionado.value = instituto
    }

    // ----- Onboarding vocacional -----
    private val _perguntaAtualIndex = MutableStateFlow(0)
    val perguntaAtualIndex: StateFlow<Int> = _perguntaAtualIndex.asStateFlow()

    private val _respostasOnboarding = MutableStateFlow<List<PerfilHolland>>(emptyList())

    val onboardingConcluido: StateFlow<Boolean> = _respostasOnboarding
        .map { respostas -> respostas.size >= perguntasOnboarding.size }
        .stateIn(viewModelScope, enquantoObservado, false)

    /** Só faz sentido voltar se já existe alguma resposta registrada. */
    val podeDesfazerOnboarding: StateFlow<Boolean> = _respostasOnboarding
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, enquantoObservado, false)

    fun responderOnboarding(opcao: OpcaoOnboarding) {
        // Guarda: sem isto, tocar numa opção depois da última pergunta continuaria
        // acumulando respostas e distorceria o perfil dominante.
        if (_respostasOnboarding.value.size >= perguntasOnboarding.size) return
        _respostasOnboarding.value = _respostasOnboarding.value + opcao.perfil
        if (_perguntaAtualIndex.value < perguntasOnboarding.lastIndex) {
            _perguntaAtualIndex.value = _perguntaAtualIndex.value + 1
        }
        if (_respostasOnboarding.value.size >= perguntasOnboarding.size) {
            desbloquearBadge("badge-vocacao", "Vocação Definida")
        }
    }

    /** Desfaz a última resposta e volta uma pergunta. */
    fun desfazerUltimaResposta() {
        val respostas = _respostasOnboarding.value
        if (respostas.isEmpty()) return
        _respostasOnboarding.value = respostas.dropLast(1)
        if (_perguntaAtualIndex.value > 0) {
            _perguntaAtualIndex.value = _perguntaAtualIndex.value - 1
        }
    }

    fun perfilDominante(): PerfilHolland? =
        _respostasOnboarding.value.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

    // ----- Estado vivo, vindo do repositório -----
    val aulas: StateFlow<List<Aula>> = repository.aulas
    val missoes: StateFlow<List<Missao>> = repository.missoesDaSemana
    val xpAtual: StateFlow<Int> = repository.xpAtual
    val badges: StateFlow<List<Badge>> = repository.badges
    val certificados: StateFlow<List<Certificado>> = repository.certificados
    val checkInsRecentes: StateFlow<List<CheckInRegistro>> = repository.checkInsRecentes

    val aulaDeHoje: StateFlow<Aula?> = aulas
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, enquantoObservado, repository.aulas.value.firstOrNull())

    /** As próximas aulas da agenda — tudo menos a de hoje. */
    val proximasAulas: StateFlow<List<Aula>> = aulas
        .map { it.drop(1) }
        .stateIn(viewModelScope, enquantoObservado, emptyList())

    fun aulaPorId(id: String): Aula? = repository.aulas.value.find { it.id == id }

    val missoesConcluidas: StateFlow<Int> = missoes
        .map { lista -> lista.count { it.concluida } }
        .stateIn(viewModelScope, enquantoObservado, 0)

    val badgesConquistados: StateFlow<Int> = badges
        .map { lista -> lista.count { it.conquistada } }
        .stateIn(viewModelScope, enquantoObservado, 0)

    val progressoNivel: StateFlow<Float> = xpAtual
        .map { xp -> progressoDeXp(xp, aluno.xpProximoNivel) }
        .stateIn(viewModelScope, enquantoObservado, progressoDeXp(aluno.xpAtual, aluno.xpProximoNivel))

    val xpParaProximoNivel: StateFlow<Int> = xpAtual
        .map { xp -> xpRestante(xp, aluno.xpProximoNivel) }
        .stateIn(viewModelScope, enquantoObservado, xpRestante(aluno.xpAtual, aluno.xpProximoNivel))

    // ----- Filtro do hub de conteúdo -----
    private val _areaSelecionada = MutableStateFlow<AreaTrilha?>(null)
    val areaSelecionada: StateFlow<AreaTrilha?> = _areaSelecionada.asStateFlow()

    private val _buscaTrilha = MutableStateFlow("")
    val buscaTrilha: StateFlow<String> = _buscaTrilha.asStateFlow()

    /**
     * As trilhas visíveis no hub. É estado derivado de dois filtros, não uma cópia da
     * lista: a fonte continua sendo o catálogo estático do repositório.
     */
    val trilhasFiltradas: StateFlow<List<Trilha>> =
        combine(_areaSelecionada, _buscaTrilha) { area, busca ->
            filtrarTrilhas(area, busca)
        }.stateIn(viewModelScope, enquantoObservado, trilhas)

    fun filtrarTrilhas(area: AreaTrilha?, busca: String): List<Trilha> =
        trilhas.filter { trilha ->
            val combinaArea = area == null || trilha.area == area
            val combinaBusca = busca.isBlank() || trilha.nome.contains(busca.trim(), ignoreCase = true)
            combinaArea && combinaBusca
        }

    /** Tocar de novo na área já selecionada limpa o filtro. */
    fun selecionarArea(area: AreaTrilha?) {
        _areaSelecionada.value = if (_areaSelecionada.value == area) null else area
    }

    fun buscarTrilha(termo: String) {
        _buscaTrilha.value = termo
    }

    // ----- Ações que mudam o estado -----

    /**
     * Regra de negócio do acerto no quiz: soma o XP e avança a missão de quizzes.
     * O repositório só grava o resultado — a decisão é daqui.
     */
    fun registrarAcertoQuiz(quiz: Quiz) {
        somarXp(quiz.xp)
        avisar("Boa! +${quiz.xp} XP")
        progredirMissao(TipoMissao.QUIZ)
    }

    /** Confirma presença na aula, avança a missão de aulas e soma o XP dela. */
    fun confirmarPresenca(aulaId: String) {
        val aula = repository.aulas.value.find { it.id == aulaId } ?: return
        if (aula.presencaConfirmada) return

        repository.atualizarAulas(
            repository.aulas.value.map {
                if (it.id == aulaId) it.copy(presencaConfirmada = true) else it
            }
        )
        somarXp(XP_PRESENCA)
        avisar("Presença confirmada em ${aula.titulo} · +$XP_PRESENCA XP")
        progredirMissao(TipoMissao.AULA)
    }

    /** Publica o certificado no Perfil Vivo. */
    fun publicarCertificado(certificadoId: String) {
        val certificado = repository.certificados.value.find { it.id == certificadoId } ?: return
        if (certificado.publicado) return

        repository.atualizarCertificados(
            repository.certificados.value.map {
                if (it.id == certificadoId) it.copy(publicado = true) else it
            }
        )
        avisar("${certificado.nome} publicado no seu Perfil Vivo")
    }

    /**
     * Registra o check-in do dia no topo da lista, avança a missão de check-in e soma XP.
     * Substitui o antigo `enviarCheckIn()`, que só ligava um booleano.
     */
    fun registrarCheckIn(
        humor: Humor,
        carga: CargaTarefas,
        comentario: String,
        data: Date = Date()
    ) {
        repository.registrarCheckIn(
            CheckInRegistro(
                data = formatarDataDeCheckIn(data),
                humor = humor,
                cargaTarefas = carga,
                comentario = comentario.trim()
            )
        )
        _checkInEnviado.value = true
        somarXp(XP_CHECKIN)
        avisar("Check-in registrado · +$XP_CHECKIN XP")
        progredirMissao(TipoMissao.CHECKIN)
    }

    // ----- Check-in emocional -----
    private val _checkInEnviado = MutableStateFlow(false)
    val checkInEnviado: StateFlow<Boolean> = _checkInEnviado.asStateFlow()

    /**
     * Devolve a tela de check-in ao formulário vazio. Chamada quando o aluno **entra**
     * na tela, e não logo após o envio: a confirmação precisa continuar visível até ele
     * decidir voltar.
     */
    fun resetCheckIn() {
        _checkInEnviado.value = false
    }

    // ----- Regras internas de gamificação -----

    private fun somarXp(xp: Int) {
        repository.definirXpAtual(repository.xpAtual.value + xp)
    }

    /**
     * Avança em um a missão daquele tipo. Ao concluí-la, desbloqueia o badge
     * correspondente — a busca é por [TipoMissao], não por id literal.
     */
    private fun progredirMissao(tipo: TipoMissao) {
        val missao = repository.missoesDaSemana.value.find { it.tipo == tipo && !it.concluida }
            ?: return

        val novoProgresso = (missao.progressoAtual + 1).coerceAtMost(missao.progressoTotal)
        val concluiuAgora = novoProgresso >= missao.progressoTotal

        repository.atualizarMissoes(
            repository.missoesDaSemana.value.map {
                if (it.id == missao.id) {
                    it.copy(progressoAtual = novoProgresso, concluida = concluiuAgora)
                } else {
                    it
                }
            }
        )

        if (concluiuAgora) {
            somarXp(missao.xp)
            avisar("Missão concluída: ${missao.titulo} · +${missao.xp} XP")
            badgeDaMissao(tipo)?.let { (id, nome) -> desbloquearBadge(id, nome) }
        }
    }

    /** Nem todo tipo de missão tem badge correspondente no mural — o check-in não tem. */
    private fun badgeDaMissao(tipo: TipoMissao): Pair<String, String>? = when (tipo) {
        TipoMissao.QUIZ -> "badge-quiz" to "Quiz Master"
        TipoMissao.AULA -> "badge-presenca" to "Presença Perfeita"
        TipoMissao.FORUM -> "badge-mentor" to "Mentor"
        TipoMissao.CHECKIN -> null
    }

    private fun desbloquearBadge(badgeId: String, nomeBadge: String) {
        val badge = repository.badges.value.find { it.id == badgeId } ?: return
        if (badge.conquistada) return

        repository.atualizarBadges(
            repository.badges.value.map {
                if (it.id == badgeId) it.copy(conquistada = true) else it
            }
        )
        avisar("Badge desbloqueado: $nomeBadge")
    }

    private companion object {
        const val XP_PRESENCA = 40
        const val XP_CHECKIN = 20
    }
}
