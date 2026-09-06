package com.fiap.edutrack.ui.screens.conquistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.ui.components.*
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.util.formatarXp
import com.fiap.edutrack.util.progressoDeXp
import com.fiap.edutrack.util.xpRestante
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * T05 · Conquistas / Gamificação.
 * Sistema de XP, conquistas (badges) e missões da semana — calibrado para motivar sem
 * virar competição tóxica. Reconhece esforço, não só resultado.
 */
@Composable
fun ConquistasScreen(viewModel: EduTrackViewModel) {
    val aluno = viewModel.aluno
    val badges by viewModel.badges.collectAsStateWithLifecycle()
    val badgesConquistados by viewModel.badgesConquistados.collectAsStateWithLifecycle()
    val missoes by viewModel.missoes.collectAsStateWithLifecycle()
    val xpAtual by viewModel.xpAtual.collectAsStateWithLifecycle()

    val xpEmJogo = missoes.filterNot { it.concluida }.sumOf { it.xp }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Cabeçalho sangrado: ocupa a largura inteira, sem o respiro lateral da tela.
        CabecalhoNivel(
            nivel = aluno.nivel,
            tituloNivel = aluno.tituloNivel,
            xpAtual = xpAtual,
            xpProximo = aluno.xpProximoNivel
        )

        Column(modifier = Modifier.padding(20.dp)) {
            BarraSecao("Mural de badges", valorDireita = "$badgesConquistados/${badges.size} conquistadas")
            Spacer(Modifier.height(12.dp))
            badges.chunked(3).forEach { linha ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    linha.forEach { badge ->
                        BadgeCircular(badge = badge, modifier = Modifier.weight(1f))
                    }
                    repeat(3 - linha.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            BarraSecao(
                "Missões da semana",
                valorDireita = if (xpEmJogo > 0) "+$xpEmJogo XP em jogo" else "tudo concluído"
            )
            Spacer(Modifier.height(12.dp))
            missoes.forEach { missao ->
                ItemMissao(missao = missao, modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}

/** Faixa de nível em gradiente, sangrada até as bordas da tela. */
@Composable
private fun CabecalhoNivel(
    nivel: Int,
    tituloNivel: String,
    xpAtual: Int,
    xpProximo: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GradienteMarca)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Conquistas",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            EduTrackChip(
                texto = "${formatarXp(xpAtual)} XP total",
                corFundo = Amber400,
                corTexto = Amber800,
                icone = Icons.Filled.Star
            )
        }

        Spacer(Modifier.height(18.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "NÍVEL",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.weight(1f)
            )
            Text(
                "PRÓXIMO",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                nivel.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Spacer(Modifier.width(10.dp))
            Text(
                tituloNivel,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                (nivel + 1).toString(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White.copy(alpha = 0.35f)
            )
        }

        Spacer(Modifier.height(14.dp))
        EduTrackProgressBar(progresso = progressoDeXp(xpAtual, xpProximo), cor = Amber400)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${formatarXp(xpAtual)} / ${formatarXp(xpProximo)} XP",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "${xpRestante(xpAtual, xpProximo)} XP restantes",
                style = MaterialTheme.typography.labelMedium,
                color = Amber400,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
