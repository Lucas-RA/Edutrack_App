package com.fiap.edutrack.ui.screens.checkin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.data.model.CargaTarefas
import com.fiap.edutrack.data.model.Humor
import com.fiap.edutrack.ui.components.*
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.util.formatarDataDeCheckIn
import com.fiap.edutrack.viewmodel.EduTrackViewModel
import java.util.Date

/**
 * T06 · Check-in emocional.
 * Termômetro diário rápido que alimenta o painel do educador com sinais precoces de
 * sobrecarga ou queda. Linguagem afetiva, não clínica. As respostas não são vistas pelos
 * professores — e esse contrato aparece na própria tela, de propósito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    viewModel: EduTrackViewModel,
    aoConcluir: () -> Unit
) {
    // Ao entrar na tela, o formulário volta ao início. Fica aqui, e não no envio, para
    // que a confirmação continue visível até o aluno tocar em "Voltar para o início".
    LaunchedEffect(Unit) {
        viewModel.resetCheckIn()
    }

    val checkInEnviado by viewModel.checkInEnviado.collectAsStateWithLifecycle()

    var humorSelecionado by remember { mutableStateOf<Humor?>(null) }
    var cargaSelecionada by remember { mutableStateOf<CargaTarefas?>(null) }
    var comentario by remember { mutableStateOf("") }
    val dataDeHoje = remember { formatarDataDeCheckIn(Date()).uppercase() }

    if (checkInEnviado) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Check-in enviado 💚",
                style = MaterialTheme.typography.headlineMedium,
                color = Green900
            )
            Spacer(Modifier.height(8.dp))
            Text("Obrigado por compartilhar como você está.", color = InkMuted)
            Spacer(Modifier.height(20.dp))
            EduTrackButton("Voltar para o início", onClick = aoConcluir)
        }
        return
    }

    val podeEnviar = humorSelecionado != null && cargaSelecionada != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = aoConcluir) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.dp, BordaSutil),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Lock,
                                contentDescription = null,
                                tint = Green800,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Anônimo",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Green800
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                RodapeAcoes(
                    textoSecundario = "Pular hoje",
                    aoSecundario = aoConcluir,
                    textoPrimario = "Enviar check-in",
                    habilitado = podeEnviar,
                    aoPrimario = {
                        val humor = humorSelecionado
                        val carga = cargaSelecionada
                        if (humor != null && carga != null) {
                            viewModel.registrarCheckIn(humor, carga, comentario)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "CHECK-IN · $dataDeHoje",
                style = MaterialTheme.typography.labelMedium,
                color = InkMuted
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Como você está, de verdade?",
                style = MaterialTheme.typography.headlineMedium,
                color = Green900
            )
            Spacer(Modifier.height(10.dp))
            // Este texto é a constraint de privacidade aparecendo na interface. Não cortar.
            Text(
                "Suas respostas não são vistas pelos professores.\n" +
                    "Servem só pra você e pro time de cuidado.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )

            Spacer(Modifier.height(20.dp))
            EduTrackCard {
                TituloDoBloco(numero = "1", texto = "Como você se sente hoje?")
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Humor.values().forEach { humor ->
                        BotaoHumor(
                            humor = humor,
                            selecionado = humorSelecionado == humor,
                            aoClicar = { humorSelecionado = humor }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            EduTrackCard {
                TituloDoBloco(numero = "2", texto = "E sobre a carga de tarefas?")
                Spacer(Modifier.height(14.dp))
                CargaTarefas.values().forEach { carga ->
                    OpcaoSelecionavel(
                        titulo = carga.rotulo,
                        subtitulo = carga.descricao,
                        icone = iconeDaCarga(carga),
                        selecionado = cargaSelecionada == carga,
                        aoClicar = { cargaSelecionada = carga },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            EduTrackCard {
                TituloDoBloco(numero = "3", texto = "Quer contar algo? (opcional)")
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    placeholder = { Text("Escreva livremente...") }
                )
            }

            if (!podeEnviar) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Escolha como você se sente e como está a carga para enviar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

private fun iconeDaCarga(carga: CargaTarefas): ImageVector = when (carga) {
    CargaTarefas.TRANQUILO -> Icons.Filled.Favorite
    CargaTarefas.ALGUMAS_PESAM -> Icons.Filled.TrackChanges
    CargaTarefas.SOBRECARREGADO -> Icons.Filled.LocalFireDepartment
}

@Composable
private fun TituloDoBloco(numero: String, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Green100),
            contentAlignment = Alignment.Center
        ) {
            Text(
                numero,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Green800
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BotaoHumor(humor: Humor, selecionado: Boolean, aoClicar: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            onClick = aoClicar,
            shape = CircleShape,
            color = if (selecionado) Green800 else Ink50,
            border = BorderStroke(1.dp, if (selecionado) Green800 else BordaSutil),
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(humor.emoji, fontSize = 26.sp)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            humor.rotulo,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            color = if (selecionado) Green900 else InkMuted
        )
    }
}
