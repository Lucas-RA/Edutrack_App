package com.fiap.edutrack.ui.screens.conteudo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.data.model.AreaTrilha
import com.fiap.edutrack.data.model.Trilha
import com.fiap.edutrack.ui.components.*
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * T04 · Hub de conteúdo.
 * Catálogo de aulas e quizzes vindos do Moodle (simulado), organizados por trilha e
 * adaptados ao perfil vocacional do aluno. Trilhas filtráveis por área e por busca.
 */
@Composable
fun ConteudoScreen(
    viewModel: EduTrackViewModel,
    aoAbrirTrilha: (String) -> Unit
) {
    val trilhas by viewModel.trilhasFiltradas.collectAsStateWithLifecycle()
    val areaSelecionada by viewModel.areaSelecionada.collectAsStateWithLifecycle()
    val busca by viewModel.buscaTrilha.collectAsStateWithLifecycle()

    var buscaAberta by remember { mutableStateOf(false) }
    val trilhaEmDestaque = viewModel.trilhaEmDestaque
    val quizEmDestaque = trilhaEmDestaque?.let { viewModel.quizzesDaTrilha(it.id).firstOrNull() }

    // Cabeçalho e filtros vivem dentro da própria LazyColumn: fora dela, com a fonte do
    // sistema ampliada, eles espremeriam a lista até sobrar altura para nenhum cartão.
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("CONTEÚDO", style = MaterialTheme.typography.labelMedium, color = InkMuted)
                    Text(
                        "Hub de aprendizagem",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Green900
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    border = BorderStroke(1.dp, BordaSutil)
                ) {
                    IconButton(
                        onClick = {
                            buscaAberta = !buscaAberta
                            if (!buscaAberta) viewModel.buscarTrilha("")
                        }
                    ) {
                        Icon(
                            imageVector = if (buscaAberta) Icons.Filled.Close else Icons.Filled.Search,
                            contentDescription = if (buscaAberta) "Fechar busca" else "Buscar trilha",
                            tint = Green800
                        )
                    }
                }
            }
        }

        if (buscaAberta) {
            item {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { viewModel.buscarTrilha(it) },
                    singleLine = true,
                    label = { Text("Buscar trilha") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            // Filtro por área numa fileira rolável na horizontal, como na referência.
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    ChipFiltro(
                        texto = "Todas",
                        selecionado = areaSelecionada == null,
                        aoClicar = { viewModel.selecionarArea(null) }
                    )
                }
                items(AreaTrilha.values()) { area ->
                    ChipFiltro(
                        texto = area.rotulo,
                        selecionado = areaSelecionada == area,
                        aoClicar = { viewModel.selecionarArea(area) }
                    )
                }
            }
        }

        if (trilhaEmDestaque != null) {
            item { BarraSecao("Continuar trilha") }
            item {
                CardContinuarTrilha(
                    trilha = trilhaEmDestaque,
                    aoAbrir = { aoAbrirTrilha(trilhaEmDestaque.id) }
                )
            }

            if (quizEmDestaque != null) {
                item {
                    BarraSecao("Quiz disponível", valorDireita = "★ +${quizEmDestaque.xp} XP")
                }
                item {
                    EduTrackCard(
                        modifier = Modifier.clickableCard { aoAbrirTrilha(trilhaEmDestaque.id) }
                    ) {
                        Text(
                            "PERGUNTA 1 DE ${viewModel.quizzesDaTrilha(trilhaEmDestaque.id).size} · ${trilhaEmDestaque.area.rotulo.uppercase()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = InkMuted
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            quizEmDestaque.pergunta,
                            style = MaterialTheme.typography.titleLarge,
                            color = Green900
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Toque para responder e ganhar XP",
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMuted
                        )
                    }
                }
            }
        }

        item { BarraSecao("Todas as trilhas", valorDireita = trilhas.size.toString()) }

        if (trilhas.isEmpty()) {
            item {
                Text(
                    "Nenhuma trilha encontrada com esse filtro.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkMuted
                )
            }
        } else {
            items(trilhas) { trilha ->
                CartaoTrilha(trilha = trilha, onClick = { aoAbrirTrilha(trilha.id) })
            }
        }
    }
}

/** Card em gradiente com a trilha de maior progresso — o "continue de onde parou". */
@Composable
private fun CardContinuarTrilha(trilha: Trilha, aoAbrir: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GradienteMarca)
            .clickableCard(aoAbrir)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconeQuadrado(
                icone = Icons.Filled.Code,
                corFundo = Color.White.copy(alpha = 0.2f),
                corIcone = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "TRILHA · ${trilha.area.rotulo.uppercase()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.75f)
                )
                Text(
                    trilha.nome,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Módulo ${trilha.moduloAtual} de ${trilha.totalModulos} · ${trilha.aulasRestantes} aulas restantes",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Progresso",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${(trilha.progresso * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(6.dp))
        EduTrackProgressBar(progresso = trilha.progresso, cor = Amber400)
    }
}

@Composable
private fun CartaoTrilha(trilha: Trilha, onClick: () -> Unit) {
    EduTrackCard(modifier = Modifier.clickableCard(onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EduTrackChip(trilha.area.rotulo)
            Text(
                "Módulo ${trilha.moduloAtual} de ${trilha.totalModulos}",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(trilha.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        EduTrackProgressBar(progresso = trilha.progresso, cor = Green600)
        Spacer(Modifier.height(4.dp))
        Text(
            if (trilha.aulasRestantes > 0) "${trilha.aulasRestantes} aulas restantes" else "Trilha concluída",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}
