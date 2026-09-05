package com.fiap.edutrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.ui.theme.*

/**
 * Bloco de cabeçalho com o gradiente da marca: rótulo mono em branco esmaecido,
 * título serifado em branco e um slot opcional de ação à direita.
 */
@Composable
fun CabecalhoGradiente(
    rotulo: String,
    titulo: String,
    modifier: Modifier = Modifier,
    acao: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GradienteMarca)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                rotulo.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
        if (acao != null) {
            Spacer(Modifier.width(12.dp))
            acao()
        }
    }
}

/**
 * Cabeçalho de seção: rótulo mono em caixa alta à esquerda e um contador à direita
 * ("MISSÕES DA SEMANA · 1/4"). Substitui o par Text+Row repetido nas telas.
 */
@Composable
fun BarraSecao(
    titulo: String,
    modifier: Modifier = Modifier,
    valorDireita: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            titulo.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted,
            modifier = Modifier.weight(1f)
        )
        if (valorDireita != null) {
            Spacer(Modifier.width(8.dp))
            Text(
                valorDireita,
                style = MaterialTheme.typography.labelMedium,
                color = InkMuted,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Trilho de etapas do onboarding: círculo com check na etapa concluída, número em verde
 * na etapa atual e cinza nas futuras, ligados por uma linha.
 */
@Composable
fun StepperEtapas(
    etapas: List<String>,
    indiceAtual: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            etapas.forEachIndexed { indice, _ ->
                val concluida = indice < indiceAtual
                val atual = indice == indiceAtual
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                concluida -> Green600
                                atual -> Green800
                                else -> Slate100
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (concluida) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            (indice + 1).toString(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (atual) Color.White else InkMuted
                        )
                    }
                }
                if (indice < etapas.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp),
                        thickness = 2.dp,
                        color = if (indice < indiceAtual) Green600 else Slate100
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            etapas.forEachIndexed { indice, nome ->
                Text(
                    nome,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (indice == indiceAtual) FontWeight.Bold else FontWeight.Normal,
                    color = if (indice <= indiceAtual) Green900 else InkMuted,
                    modifier = if (indice == etapas.lastIndex) Modifier else Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PreviaCabecalhoGradiente() {
    EduTrackTheme {
        Column(Modifier.padding(16.dp)) {
            CabecalhoGradiente(
                rotulo = "Hub de aprendizagem",
                titulo = "Trilhas do seu perfil",
                acao = { EduTrackChip("4 trilhas", corFundo = Color.White) }
            )
            Spacer(Modifier.height(16.dp))
            BarraSecao("Missões da semana", valorDireita = "1/4")
            Spacer(Modifier.height(16.dp))
            StepperEtapas(listOf("Dados", "Vocação", "Trilha"), indiceAtual = 1)
        }
    }
}
