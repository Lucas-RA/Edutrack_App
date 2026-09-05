package com.fiap.edutrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.edutrack.ui.theme.BrandTeal
import com.fiap.edutrack.ui.theme.Green800
import com.fiap.edutrack.util.iniciaisDoNome

/** Avatar circular com as iniciais do nome, em gradiente da marca. */
@Composable
fun EduTrackAvatar(nome: String, tamanho: Dp = 48.dp) {
    Box(
        modifier = Modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(BrandTeal, Green800))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            iniciaisDoNome(nome),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (tamanho.value / 2.6).sp
        )
    }
}

/** Barra de progresso com trilho arredondado, no padrão de gamificação do app. */
@Composable
fun EduTrackProgressBar(progresso: Float, cor: Color = BrandTeal) {
    LinearProgressIndicator(
        progress = { progresso.coerceIn(0f, 1f) },
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(50)),
        color = cor,
        trackColor = cor.copy(alpha = 0.15f)
    )
}

/** Selo quadrado de 40dp usado como marcador de categoria em listas e cartões. */
@Composable
fun IconeQuadrado(
    icone: ImageVector,
    corFundo: Color,
    corIcone: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(corFundo),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = null, tint = corIcone, modifier = Modifier.size(20.dp))
    }
}

/** Marca de item concluído — presença confirmada, missão fechada. */
@Composable
fun IconeConcluido() {
    Icon(
        Icons.Filled.CheckCircle,
        contentDescription = "Concluída",
        tint = BrandTeal,
        modifier = Modifier.size(18.dp)
    )
}
