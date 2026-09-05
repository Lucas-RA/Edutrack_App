package com.fiap.edutrack.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.navigation.Rotas
import com.fiap.edutrack.ui.components.*
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * T03 · Home do aluno.
 * Centro de comando diário: nível e XP, próxima aula, missões da semana e atalhos
 * para conteúdo, gamificação, check-in e perfil. O que fazer agora vem primeiro.
 */
@Composable
fun HomeScreen(
    viewModel: EduTrackViewModel,
    aoAbrirCheckIn: () -> Unit,
    aoAbrirAula: (String) -> Unit,
    aoNavegar: (String) -> Unit
) {
    val aluno = viewModel.aluno
    val aula by viewModel.aulaDeHoje.collectAsStateWithLifecycle()
    val proximasAulas by viewModel.proximasAulas.collectAsStateWithLifecycle()
    val missoes by viewModel.missoes.collectAsStateWithLifecycle()
    val concluidas by viewModel.missoesConcluidas.collectAsStateWithLifecycle()
    val xpAtual by viewModel.xpAtual.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            EduTrackAvatar(aluno.nome)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Bom dia,", style = MaterialTheme.typography.bodyMedium, color = InkMuted)
                Text(
                    "${aluno.nome.substringBefore(" ")} 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Green900
                )
            }
            SinoComAviso()
        }

        Spacer(Modifier.height(10.dp))
        StreakBadge(aluno.streakDias)

        Spacer(Modifier.height(16.dp))
        CardNivel(
            nivel = aluno.nivel,
            titulo = aluno.tituloNivel,
            xpAtual = xpAtual,
            xpProximo = aluno.xpProximoNivel
        )

        Spacer(Modifier.height(20.dp))
        BarraSecao("Aula de hoje")
        Spacer(Modifier.height(8.dp))
        aula?.let { aulaDeHoje ->
            EduTrackCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconeQuadrado(
                        icone = Icons.AutoMirrored.Filled.MenuBook,
                        corFundo = Green100,
                        corIcone = Green800
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "${aulaDeHoje.instituto} · ${aulaDeHoje.horario}",
                            style = MaterialTheme.typography.labelMedium,
                            color = InkMuted
                        )
                        Text(
                            aulaDeHoje.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                if (aulaDeHoje.presencaConfirmada) {
                    EduTrackChip("● Presença confirmada", corFundo = Green100, corTexto = Green800)
                } else {
                    EduTrackChip("● Presença em aberto", corFundo = Amber100, corTexto = Amber800)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { aoAbrirAula(aulaDeHoje.id) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                ) {
                    Text("Ver detalhes da aula", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (proximasAulas.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            BarraSecao("Próximas aulas", valorDireita = proximasAulas.size.toString())
            Spacer(Modifier.height(8.dp))
            proximasAulas.forEach { proxima ->
                EduTrackCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickableCard { aoAbrirAula(proxima.id) }
                ) {
                    Text(
                        "${proxima.instituto} · ${proxima.data} · ${proxima.horario}",
                        style = MaterialTheme.typography.labelMedium,
                        color = InkMuted
                    )
                    Text(
                        proxima.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (proxima.presencaConfirmada) {
                        Spacer(Modifier.height(6.dp))
                        EduTrackChip("● Presença confirmada", corFundo = Green100, corTexto = Green800)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        BarraSecao("Missões da semana", valorDireita = "$concluidas/${missoes.size}")
        Spacer(Modifier.height(8.dp))
        missoes.take(2).forEach { missao ->
            ItemMissao(missao = missao, modifier = Modifier.padding(bottom = 8.dp))
        }

        Spacer(Modifier.height(20.dp))
        BarraSecao("Atalhos")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Atalho(
                icone = Icons.Filled.School,
                rotulo = "Conteúdo",
                corFundo = Green100,
                corIcone = Green800,
                aoClicar = { aoNavegar(Rotas.CONTEUDO) },
                modifier = Modifier.weight(1f)
            )
            Atalho(
                icone = Icons.Filled.EmojiEvents,
                rotulo = "Conquistas",
                corFundo = Amber100,
                corIcone = Amber800,
                aoClicar = { aoNavegar(Rotas.CONQUISTAS) },
                modifier = Modifier.weight(1f)
            )
            Atalho(
                icone = Icons.Filled.Favorite,
                rotulo = "Check-in",
                corFundo = Green100,
                corIcone = BrandTeal,
                aoClicar = aoAbrirCheckIn,
                modifier = Modifier.weight(1f)
            )
            Atalho(
                icone = Icons.Filled.Person,
                rotulo = "Perfil",
                corFundo = Slate100,
                corIcone = InkMuted,
                aoClicar = { aoNavegar(Rotas.PERFIL) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = aoAbrirCheckIn,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Fazer check-in emocional de hoje")
        }
        Spacer(Modifier.height(8.dp))
    }
}

/** Sino de notificações com o ponto vermelho de aviso, como na referência. */
@Composable
private fun SinoComAviso() {
    Box {
        Surface(
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, BordaSutil)
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificações", tint = Green800)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(Red500)
        )
    }
}

/** Quadrado clicável da fileira de atalhos. */
@Composable
private fun Atalho(
    icone: ImageVector,
    rotulo: String,
    corFundo: Color,
    corIcone: Color,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = aoClicar,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordaSutil),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconeQuadrado(icone = icone, corFundo = corFundo, corIcone = corIcone)
            Spacer(Modifier.height(6.dp))
            Text(
                rotulo,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
