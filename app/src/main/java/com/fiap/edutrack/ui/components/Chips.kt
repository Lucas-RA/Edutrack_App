package com.fiap.edutrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.edutrack.ui.theme.Amber100
import com.fiap.edutrack.ui.theme.Amber800
import com.fiap.edutrack.ui.theme.Green100
import com.fiap.edutrack.ui.theme.Green900

/** Chip de destaque (XP, streak, badge) com fundo suave e ícone opcional. */
@Composable
fun EduTrackChip(
    texto: String,
    corFundo: Color = Green100,
    corTexto: Color = Green900,
    icone: ImageVector? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(corFundo)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        icone?.let {
            Icon(it, contentDescription = null, tint = corTexto, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
        }
        Text(texto, color = corTexto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Selo de streak (dias seguidos de acesso), com ícone de chama. */
@Composable
fun StreakBadge(dias: Int) {
    EduTrackChip(
        texto = "$dias dias seguidos",
        corFundo = Amber100,
        corTexto = Amber800,
        icone = Icons.Filled.LocalFireDepartment
    )
}
