package com.fiap.edutrack.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.ui.theme.*

/** Chip de filtro por categoria, no verde da marca quando selecionado. */
@Composable
fun ChipFiltro(
    texto: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selecionado,
        onClick = aoClicar,
        label = { Text(texto) },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Green100,
            selectedLabelColor = Green900
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selecionado,
            borderColor = BordaSutil,
            selectedBorderColor = Green800,
            selectedBorderWidth = 2.dp
        ),
        modifier = modifier
    )
}

/**
 * Opção de lista com estado de escolha: borda verde de 2dp, fundo verde claro e um
 * check circular quando selecionada. Serve ao onboarding e à carga de tarefas do check-in.
 */
@Composable
fun OpcaoSelecionavel(
    titulo: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icone: ImageVector? = null,
    letra: String? = null
) {
    Card(
        onClick = aoClicar,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selecionado) Green100 else Ink50
        ),
        border = BorderStroke(
            width = if (selecionado) 2.dp else 1.dp,
            color = if (selecionado) Green800 else BordaSutil
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (letra != null) {
                LetraQuadrada(letra = letra, selecionado = selecionado)
                Spacer(Modifier.width(12.dp))
            } else if (icone != null) {
                IconeQuadrado(
                    icone = icone,
                    corFundo = if (selecionado) Green800 else Slate100,
                    corIcone = if (selecionado) Ink50 else InkMuted
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(
                    titulo,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selecionado) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selecionado) Green900 else MaterialTheme.colorScheme.onSurface
                )
                if (subtitulo != null) {
                    Text(
                        subtitulo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMuted
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (selecionado) Icons.Filled.CheckCircle
                    else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = if (selecionado) "Selecionado" else "Não selecionado",
                tint = if (selecionado) Green800 else InkMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/** Selo quadrado com a letra da alternativa (A, B, C, D), no padrão da referência. */
@Composable
private fun LetraQuadrada(letra: String, selecionado: Boolean) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) Green800 else Slate100),
        contentAlignment = Alignment.Center
    ) {
        Text(
            letra,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (selecionado) Ink50 else InkMuted
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PreviaSelecao() {
    EduTrackTheme {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro("Lógica", selecionado = true, aoClicar = {})
                ChipFiltro("Carreira", selecionado = false, aoClicar = {})
            }
            Spacer(Modifier.height(16.dp))
            OpcaoSelecionavel(
                titulo = "Apresentar uma ideia para um grupo",
                subtitulo = "Perfil empreendedor",
                letra = "C",
                selecionado = true,
                aoClicar = {}
            )
            Spacer(Modifier.height(8.dp))
            OpcaoSelecionavel(
                titulo = "Tranquilo(a), consigo dar conta",
                selecionado = false,
                aoClicar = {}
            )
        }
    }
}
