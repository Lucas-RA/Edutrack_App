package com.fiap.edutrack.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.ui.components.EduTrackButton
import com.fiap.edutrack.ui.components.EduTrackProgressBar
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.viewmodel.EduTrackViewModel

private val letras = listOf("A", "B", "C", "D", "E")

/**
 * T04b · Quiz da trilha, aberto a partir do hub de conteúdo.
 * Demonstra a passagem de parâmetro entre telas (`trilhaId`) exigida pela Sprint: ao
 * tocar numa trilha na listagem, o app abre o quiz correspondente àquela trilha.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: EduTrackViewModel,
    trilhaId: String,
    aoVoltar: () -> Unit
) {
    val trilha = viewModel.trilhaPorId(trilhaId)
    val quizzes = viewModel.quizzesDaTrilha(trilhaId)
    var indiceQuiz by remember { mutableIntStateOf(0) }
    var opcaoSelecionada by remember(indiceQuiz) { mutableStateOf<Int?>(null) }
    var respondido by remember(indiceQuiz) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        trilha?.nome ?: "Quiz",
                        style = MaterialTheme.typography.titleLarge,
                        color = Green900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            if (respondido && quizzes.isNotEmpty()) {
                Surface(shadowElevation = 8.dp) {
                    EduTrackButton(
                        texto = if (indiceQuiz < quizzes.lastIndex) "Próxima pergunta" else "Voltar para a trilha",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        if (indiceQuiz < quizzes.lastIndex) indiceQuiz++ else aoVoltar()
                    }
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
            if (quizzes.isEmpty()) {
                Text("Nenhum quiz disponível para esta trilha ainda.", color = InkMuted)
                return@Column
            }

            val quiz = quizzes[indiceQuiz]

            Text(
                "PERGUNTA ${indiceQuiz + 1} DE ${quizzes.size} · ${trilha?.area?.rotulo?.uppercase().orEmpty()}",
                style = MaterialTheme.typography.labelMedium,
                color = InkMuted
            )
            Spacer(Modifier.height(8.dp))
            EduTrackProgressBar(
                progresso = (indiceQuiz + 1) / quizzes.size.toFloat(),
                cor = Green600
            )

            Spacer(Modifier.height(20.dp))
            Text(
                quiz.pergunta,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Green900
            )

            Spacer(Modifier.height(20.dp))
            quiz.alternativas.forEachIndexed { indice, alternativa ->
                val correta = indice == quiz.indiceCorreto
                val escolhida = indice == opcaoSelecionada
                val estado = when {
                    !respondido -> EstadoAlternativa.NEUTRA
                    correta -> EstadoAlternativa.CERTA
                    escolhida -> EstadoAlternativa.ERRADA
                    else -> EstadoAlternativa.NEUTRA
                }

                Alternativa(
                    letra = letras.getOrElse(indice) { "?" },
                    texto = alternativa,
                    estado = estado,
                    aoClicar = {
                        if (!respondido) {
                            opcaoSelecionada = indice
                            respondido = true
                            if (correta) viewModel.registrarAcertoQuiz(quiz)
                        }
                    },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            if (respondido) {
                Spacer(Modifier.height(4.dp))
                Text(
                    if (opcaoSelecionada == quiz.indiceCorreto) {
                        "Resposta certa! +${quiz.xp} XP"
                    } else {
                        "Não foi dessa vez — a resposta certa está em destaque."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (opcaoSelecionada == quiz.indiceCorreto) BrandTeal else Red500,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

private enum class EstadoAlternativa { NEUTRA, CERTA, ERRADA }

@Composable
private fun Alternativa(
    letra: String,
    texto: String,
    estado: EstadoAlternativa,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val corFundo = when (estado) {
        EstadoAlternativa.NEUTRA -> Ink50
        EstadoAlternativa.CERTA -> VerdeAcerto
        EstadoAlternativa.ERRADA -> VermelhoErro
    }
    val corBorda = when (estado) {
        EstadoAlternativa.NEUTRA -> BordaSutil
        EstadoAlternativa.CERTA -> Green600
        EstadoAlternativa.ERRADA -> Red500
    }
    val corSelo = when (estado) {
        EstadoAlternativa.NEUTRA -> Slate100
        EstadoAlternativa.CERTA -> Green600
        EstadoAlternativa.ERRADA -> Red500
    }

    Card(
        onClick = aoClicar,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = corFundo),
        border = BorderStroke(if (estado == EstadoAlternativa.NEUTRA) 1.dp else 2.dp, corBorda),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(corSelo),
                contentAlignment = Alignment.Center
            ) {
                when (estado) {
                    EstadoAlternativa.CERTA -> Icon(
                        Icons.Filled.Check,
                        contentDescription = "Alternativa correta",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    EstadoAlternativa.ERRADA -> Icon(
                        Icons.Filled.Close,
                        contentDescription = "Alternativa incorreta",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    EstadoAlternativa.NEUTRA -> Text(
                        letra,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = InkMuted
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                texto,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (estado == EstadoAlternativa.NEUTRA) FontWeight.Normal else FontWeight.SemiBold
            )
        }
    }
}
