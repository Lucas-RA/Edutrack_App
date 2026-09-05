package com.fiap.edutrack.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.ui.theme.BordaSutil

/** Cartão base com cantos bem arredondados e borda sutil, usado em todo o app. */
@Composable
fun EduTrackCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, BordaSutil)
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

/**
 * Torna um cartão inteiro clicável. Mora junto do cartão porque só existe para ele:
 * o `Card(onClick = ...)` do Material não serve quando o cartão é montado por
 * [EduTrackCard], que não expõe esse parâmetro.
 */
fun Modifier.clickableCard(onClick: () -> Unit): Modifier = this.clickable(onClick = onClick)
