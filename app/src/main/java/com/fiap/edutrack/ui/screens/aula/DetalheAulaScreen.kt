package com.fiap.edutrack.ui.screens.aula

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.ui.components.EduTrackButton
import com.fiap.edutrack.ui.components.EduTrackCard
import com.fiap.edutrack.ui.components.IconeConcluido
import com.fiap.edutrack.ui.theme.BrandTeal
import com.fiap.edutrack.ui.theme.Green100
import com.fiap.edutrack.ui.theme.Green800
import com.fiap.edutrack.ui.theme.Green900
import com.fiap.edutrack.ui.theme.InkMuted
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * Detalhe da aula, aberta a partir da Home pela rota `aula/{aulaId}`.
 *
 * É a segunda rota com parâmetro do app e o padrão "lista -> detalhe" que o enunciado
 * cita no critério 4: tocar num item mockado abre a tela daquele item. Confirmar presença
 * aqui progride a missão de aulas e devolve retorno visual por Snackbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheAulaScreen(
    viewModel: EduTrackViewModel,
    aulaId: String,
    aoVoltar: () -> Unit
) {
    // Observa a lista inteira para que a confirmação de presença se reflita na hora.
    val aulas by viewModel.aulas.collectAsStateWithLifecycle()
    val aula = aulas.find { it.id == aulaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe da aula") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (aula == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text("Aula não encontrada.", color = InkMuted)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(BrandTeal, Green800)))
                    .padding(20.dp)
            ) {
                Text(
                    "${aula.instituto} · ${aula.data} · ${aula.horario}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    aula.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(20.dp))
            Text("SOBRE O ENCONTRO", style = MaterialTheme.typography.labelMedium, color = InkMuted)
            Spacer(Modifier.height(8.dp))
            Text(aula.descricao, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(20.dp))
            Text("O QUE VAMOS VER", style = MaterialTheme.typography.labelMedium, color = InkMuted)
            Spacer(Modifier.height(8.dp))
            aula.topicos.forEach { topico ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("•", color = Green800, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(10.dp))
                    Text(topico, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(Modifier.height(20.dp))
            if (aula.presencaConfirmada) {
                EduTrackCard(containerColor = Green100) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconeConcluido()
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Presença confirmada",
                            color = Green900,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                EduTrackButton(
                    texto = "Confirmar presença",
                    onClick = { viewModel.confirmarPresenca(aula.id) }
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
