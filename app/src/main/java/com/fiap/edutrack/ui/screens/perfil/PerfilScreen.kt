package com.fiap.edutrack.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.data.model.Certificado
import com.fiap.edutrack.data.model.EvolucaoVocacional
import com.fiap.edutrack.ui.components.*
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.util.formatarXp
import com.fiap.edutrack.util.iniciaisDoNome
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * T07 · Perfil Vivo.
 * Portfólio dinâmico do aluno: certificados prontos para publicar, evolução do perfil
 * vocacional (Holland) e a jornada dentro do programa. É a resposta direta à dor de
 * ruptura de vínculo depois que o aluno se forma.
 */
@Composable
fun PerfilScreen(viewModel: EduTrackViewModel) {
    val aluno = viewModel.aluno
    val xpAtual by viewModel.xpAtual.collectAsStateWithLifecycle()
    val certificados by viewModel.certificados.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CabecalhoPerfil(
            nome = aluno.nome,
            subtitulo = "Egresso ${aluno.anoEgresso} · ${aluno.instituto.sigla}",
            nivel = aluno.nivel,
            xpAtual = xpAtual
        )

        Column(modifier = Modifier.padding(20.dp)) {
            BarraSecao("Certificados", valorDireita = certificados.size.toString())
            Spacer(Modifier.height(12.dp))
            certificados.forEach { certificado ->
                LinhaCertificado(certificado) { viewModel.publicarCertificado(certificado.id) }
            }

            Spacer(Modifier.height(20.dp))
            BarraSecao("Evolução vocacional · Holland")
            Spacer(Modifier.height(12.dp))
            EduTrackCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PontoLegenda(cor = InkMuted)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Entrada ${aluno.anoEgresso - 2}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMuted
                    )
                    Spacer(Modifier.width(16.dp))
                    PontoLegenda(cor = Green800)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Saída ${aluno.anoEgresso}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Green900
                    )
                }
                Spacer(Modifier.height(14.dp))
                viewModel.evolucaoVocacional.forEach { evolucao ->
                    LinhaEvolucao(evolucao)
                }
            }

            Spacer(Modifier.height(20.dp))
            BarraSecao("Sua jornada")
            Spacer(Modifier.height(12.dp))
            EduTrackCard {
                EtapaDaJornada(
                    titulo = "Onboarding vocacional",
                    detalhe = "${aluno.anoEgresso - 2} · teste Holland inicial",
                    ultima = false
                )
                EtapaDaJornada(
                    titulo = "Primeira trilha concluída",
                    detalhe = "${aluno.anoEgresso - 1} · Comunicação e Trabalho em Equipe",
                    ultima = false
                )
                EtapaDaJornada(
                    titulo = "Nível ${aluno.nivel} · ${aluno.tituloNivel}",
                    detalhe = "${aluno.anoEgresso} · ${formatarXp(xpAtual)} XP acumulados",
                    ultima = true
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** Faixa de perfil em gradiente, sangrada até as bordas, com avatar dourado. */
@Composable
private fun CabecalhoPerfil(nome: String, subtitulo: String, nivel: Int, xpAtual: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GradienteMarca)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                "PERFIL",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = Color.White)
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Amber400),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    iniciaisDoNome(nome),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Amber800
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    nome,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(8.dp))
                EduTrackChip(
                    texto = "Nível $nivel · ${formatarXp(xpAtual)} XP",
                    corFundo = Amber400,
                    corTexto = Amber800,
                    icone = Icons.Filled.Star
                )
            }
        }
    }
}

@Composable
private fun PontoLegenda(cor: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(cor)
    )
}

@Composable
private fun LinhaCertificado(certificado: Certificado, aoPublicar: () -> Unit) {
    Row(modifier = Modifier.padding(bottom = 10.dp)) {
        // Faixa âmbar vertical à esquerda, como na referência.
        Box(
            modifier = Modifier
                .width(6.dp)
                .heightIn(min = 72.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                .background(Amber400)
        )
        EduTrackCard(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconeQuadrado(
                    icone = Icons.Filled.WorkspacePremium,
                    corFundo = Amber400,
                    corIcone = Amber800
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        certificado.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${certificado.instituto} · ${certificado.dataEmissao} · ${certificado.cargaHoraria}h",
                        style = MaterialTheme.typography.labelMedium,
                        color = InkMuted
                    )
                }
                Spacer(Modifier.width(8.dp))
                if (certificado.publicado) {
                    EduTrackChip("Publicado")
                } else {
                    Button(
                        onClick = aoPublicar,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Publicar", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun LinhaEvolucao(evolucao: EvolucaoVocacional) {
    val positivo = evolucao.variacaoPercentual >= 0
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                evolucao.perfil.rotulo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (positivo) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                contentDescription = null,
                tint = if (positivo) Green600 else Red500,
                modifier = Modifier.size(16.dp)
            )
            Text(
                "${if (positivo) "+" else ""}${evolucao.variacaoPercentual}%",
                style = MaterialTheme.typography.bodyLarge,
                color = if (positivo) Green600 else Red500,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(6.dp))
        // A barra mostra a intensidade atual do perfil, com a variação já aplicada.
        EduTrackProgressBar(
            progresso = (50 + evolucao.variacaoPercentual) / 100f,
            cor = if (positivo) Green600 else InkMuted
        )
    }
}

@Composable
private fun EtapaDaJornada(titulo: String, detalhe: String, ultima: Boolean) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Green800)
            )
            if (!ultima) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(44.dp)
                        .background(Green100)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.padding(bottom = if (ultima) 0.dp else 12.dp)) {
            Text(titulo, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(detalhe, style = MaterialTheme.typography.labelMedium, color = InkMuted)
        }
    }
}
