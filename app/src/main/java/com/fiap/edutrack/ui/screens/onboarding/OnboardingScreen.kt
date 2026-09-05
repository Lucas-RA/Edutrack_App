package com.fiap.edutrack.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.data.model.PerfilHolland
import com.fiap.edutrack.ui.components.EduTrackButton
import com.fiap.edutrack.ui.components.EduTrackProgressBar
import com.fiap.edutrack.ui.components.OpcaoSelecionavel
import com.fiap.edutrack.ui.components.RodapeAcoes
import com.fiap.edutrack.ui.components.StepperEtapas
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.viewmodel.EduTrackViewModel

private val etapasOnboarding = listOf("Dados", "Vocação", "Trilha")

/**
 * T02 · Onboarding vocacional.
 * Avaliação curta baseada em Holland (RIASEC) para mapear afinidades e abrir trilhas
 * personalizadas logo no primeiro acesso. A resposta é **selecionada** e confirmada em
 * "Próxima"; "Voltar" desfaz a última. O resultado aparece na própria tela.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: EduTrackViewModel,
    aoConcluir: () -> Unit
) {
    val perguntaAtualIndex by viewModel.perguntaAtualIndex.collectAsStateWithLifecycle()
    val onboardingConcluido by viewModel.onboardingConcluido.collectAsStateWithLifecycle()
    val podeVoltar by viewModel.podeDesfazerOnboarding.collectAsStateWithLifecycle()

    val pergunta = viewModel.perguntasOnboarding[perguntaAtualIndex]
    val total = viewModel.perguntasOnboarding.size
    val progresso = (perguntaAtualIndex + 1) / total.toFloat()

    // A seleção é local e recomeça a cada pergunta — inclusive ao voltar uma pergunta.
    var opcaoSelecionada by remember(perguntaAtualIndex, onboardingConcluido) {
        mutableStateOf<Int?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (onboardingConcluido) "PASSO 3 DE 3" else "PASSO 2 DE 3",
                            style = MaterialTheme.typography.labelMedium,
                            color = InkMuted
                        )
                        Text(
                            if (onboardingConcluido) "Seu resultado" else "Descobrir vocação",
                            style = MaterialTheme.typography.titleLarge,
                            color = Green900
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.desfazerUltimaResposta() }, enabled = podeVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    TextButton(onClick = aoConcluir) { Text("Pular", color = InkMuted) }
                }
            )
        },
        bottomBar = {
            if (!onboardingConcluido) {
                Surface(shadowElevation = 8.dp) {
                    RodapeAcoes(
                        textoSecundario = if (podeVoltar) "Voltar" else null,
                        aoSecundario = if (podeVoltar) {
                            { viewModel.desfazerUltimaResposta() }
                        } else {
                            null
                        },
                        textoPrimario = "Próxima",
                        habilitado = opcaoSelecionada != null,
                        aoPrimario = {
                            opcaoSelecionada?.let { indice ->
                                viewModel.responderOnboarding(pergunta.opcoes[indice])
                            }
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            if (onboardingConcluido) {
                ResultadoVocacional(
                    perfil = viewModel.perfilDominante(),
                    aoComecar = aoConcluir
                )
                return@Column
            }

            StepperEtapas(etapas = etapasOnboarding, indiceAtual = 1)

            Spacer(Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "PERGUNTA ${perguntaAtualIndex + 1} DE $total · HOLLAND",
                    style = MaterialTheme.typography.labelMedium,
                    color = InkMuted,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "${(progresso * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = Amber800,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            EduTrackProgressBar(progresso = progresso, cor = Amber400)

            Spacer(Modifier.height(20.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Green100),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        pergunta.texto.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Green800
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Qual opção mais combina com você?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Green900
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            pergunta.opcoes.forEachIndexed { indice, opcao ->
                OpcaoSelecionavel(
                    titulo = opcao.texto,
                    subtitulo = "PERFIL: ${opcao.perfil.rotulo.uppercase()}",
                    letra = opcao.letra,
                    selecionado = opcaoSelecionada == indice,
                    aoClicar = { opcaoSelecionada = indice },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(
                "Não há resposta certa — escolha a que mais combina com você.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

/** Frase que liga o perfil dominante às trilhas — é a promessa que o pitch faz. */
private fun frasePerfil(perfil: PerfilHolland): String = when (perfil) {
    PerfilHolland.REALISTA ->
        "Você aprende fazendo. Suas trilhas priorizam prática, oficina e projetos com as mãos."
    PerfilHolland.INVESTIGATIVO ->
        "Você gosta de entender o porquê. Suas trilhas priorizam lógica, dados e resolução de problemas."
    PerfilHolland.ARTISTICO ->
        "Você pensa criando. Suas trilhas priorizam criação de conteúdo, design e comunicação visual."
    PerfilHolland.SOCIAL ->
        "Você cresce junto com gente. Suas trilhas priorizam cuidado, ensino e trabalho em equipe."
    PerfilHolland.EMPREENDEDOR ->
        "Você gosta de puxar a frente. Suas trilhas priorizam liderança, negócios e apresentação de ideias."
    PerfilHolland.CONVENCIONAL ->
        "Você organiza o caos. Suas trilhas priorizam processos, dados e gestão de rotina."
}

@Composable
private fun ResultadoVocacional(perfil: PerfilHolland?, aoComecar: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        StepperEtapas(etapas = etapasOnboarding, indiceAtual = 2)

        Spacer(Modifier.height(28.dp))
        Text(
            "SEU PERFIL DOMINANTE",
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted
        )
        Spacer(Modifier.height(8.dp))
        Text(
            perfil?.rotulo ?: "Perfil em construção",
            style = MaterialTheme.typography.displayMedium,
            color = Green900
        )
        Spacer(Modifier.height(12.dp))
        Text(
            perfil?.let { frasePerfil(it) }
                ?: "Responda ao teste para ver quais trilhas combinam com você.",
            style = MaterialTheme.typography.bodyLarge,
            color = InkMuted
        )

        Spacer(Modifier.height(28.dp))
        EduTrackButton(texto = "Começar minha jornada", onClick = aoComecar)
        Spacer(Modifier.height(12.dp))
    }
}
