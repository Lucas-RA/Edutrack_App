package com.fiap.edutrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.edutrack.navigation.Rotas
import com.fiap.edutrack.ui.theme.Green100
import com.fiap.edutrack.ui.theme.Green800

/**
 * Barra de navegação inferior — Início · Conteúdo · Conquistas · Perfil.
 * O terceiro item se chama "Conquistas", e não "Missões", porque a tela mostra nível,
 * badges **e** missões, e a rota é `conquistas`.
 */
@Composable
fun EduTrackBottomBar(rotaAtual: String, onNavegar: (String) -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        val itens = listOf(
            Triple(Rotas.HOME, "Início", Icons.Filled.Home),
            Triple(Rotas.CONTEUDO, "Conteúdo", Icons.AutoMirrored.Filled.MenuBook),
            Triple(Rotas.CONQUISTAS, "Conquistas", Icons.Filled.EmojiEvents),
            Triple(Rotas.PERFIL, "Perfil", Icons.Filled.Person)
        )
        itens.forEach { (rota, rotulo, icone) ->
            NavigationBarItem(
                selected = rotaAtual == rota,
                onClick = { onNavegar(rota) },
                icon = { Icon(icone, contentDescription = rotulo) },
                label = {
                    // Estilo explícito: o rótulo de navegação não é rótulo em caixa alta
                    // nem metadado, então não deve herdar a família monoespaçada que o
                    // Material 3 aplica por padrão em `labelMedium`.
                    Text(
                        rotulo,
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Green800,
                    selectedTextColor = Green800,
                    indicatorColor = Green100
                )
            )
        }
    }
}
