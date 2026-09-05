package com.fiap.edutrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.edutrack.ui.theme.Green800

/** Botão primário — alvo de toque de 52dp, conforme o princípio de acessibilidade. */
@Composable
fun EduTrackButton(
    texto: String,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Green800, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

/**
 * Rodapé de ações: uma ação secundária discreta à esquerda e a primária à direita.
 * O botão primário respeita [habilitado] — o mesmo padrão do envio do check-in.
 */
@Composable
fun RodapeAcoes(
    textoPrimario: String,
    aoPrimario: () -> Unit,
    modifier: Modifier = Modifier,
    textoSecundario: String? = null,
    aoSecundario: (() -> Unit)? = null,
    habilitado: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (textoSecundario != null && aoSecundario != null) {
            TextButton(onClick = aoSecundario, modifier = Modifier.weight(1f)) {
                Text(textoSecundario)
            }
        }
        EduTrackButton(
            texto = textoPrimario,
            habilitado = habilitado,
            onClick = aoPrimario,
            modifier = Modifier.weight(1f)
        )
    }
}
