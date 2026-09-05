package com.fiap.edutrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.util.formatarXp
import com.fiap.edutrack.util.progressoDeXp
import com.fiap.edutrack.util.xpRestante

/**
 * Card de nível da Home: número grande, título do nível, chip de XP com estrela,
 * barra âmbar e, embaixo, quanto falta à esquerda e a razão XP à direita.
 */
@Composable
fun CardNivel(
    nivel: Int,
    titulo: String,
    xpAtual: Int,
    xpProximo: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GradienteMarca)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                nivel.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "NÍVEL ATUAL",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
            EduTrackChip(
                texto = "${formatarXp(xpAtual)} XP",
                corFundo = Amber400,
                corTexto = Amber800,
                icone = Icons.Filled.Star
            )
        }

        Spacer(Modifier.height(14.dp))
        EduTrackProgressBar(progresso = progressoDeXp(xpAtual, xpProximo), cor = Amber400)
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Faltam ${xpRestante(xpAtual, xpProximo)} XP para o nível ${nivel + 1}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "${formatarXp(xpAtual)} / ${formatarXp(xpProximo)}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PreviaCardNivel() {
    EduTrackTheme {
        Column(Modifier.padding(16.dp)) {
            CardNivel(nivel = 7, titulo = "Explorador", xpAtual = 1240, xpProximo = 2000)
        }
    }
}
