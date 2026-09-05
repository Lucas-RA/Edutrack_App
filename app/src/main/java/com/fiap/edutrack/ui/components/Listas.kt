package com.fiap.edutrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiap.edutrack.data.model.Badge
import com.fiap.edutrack.data.model.Missao
import com.fiap.edutrack.data.model.TipoMissao
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.util.rotuloProgressoMissao

/** Ícone de cada tipo de missão — a categoria fica legível sem depender do texto. */
private fun iconeDaMissao(tipo: TipoMissao): ImageVector = when (tipo) {
    TipoMissao.QUIZ -> Icons.Filled.Quiz
    TipoMissao.CHECKIN -> Icons.Filled.Favorite
    TipoMissao.AULA -> Icons.Filled.School
    TipoMissao.FORUM -> Icons.Filled.Forum
}

/** Linha de missão: selo, título (riscado se concluída), progresso, chip de XP e barra. */
@Composable
fun ItemMissao(missao: Missao, modifier: Modifier = Modifier) {
    EduTrackCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconeQuadrado(
                icone = iconeDaMissao(missao.tipo),
                corFundo = if (missao.concluida) Green100 else Slate100,
                corIcone = if (missao.concluida) Green800 else InkMuted
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    missao.titulo,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (missao.concluida) TextDecoration.LineThrough else null
                )
                Text(
                    rotuloProgressoMissao(missao),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (missao.concluida) BrandTeal else InkMuted
                )
            }
            Spacer(Modifier.width(8.dp))
            EduTrackChip("+${missao.xp} XP", corFundo = Amber100, corTexto = Amber800)
        }

        if (!missao.concluida && missao.progressoTotal > 0) {
            Spacer(Modifier.height(10.dp))
            EduTrackProgressBar(
                progresso = missao.progressoAtual.toFloat() / missao.progressoTotal,
                cor = Amber400
            )
        }
    }
}

/** Badge em círculo de 64dp: dourado quando conquistado, cinza com cadeado quando não. */
@Composable
fun BadgeCircular(badge: Badge, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(if (badge.conquistada) Amber400 else Slate100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (badge.conquistada) Icons.Filled.EmojiEvents else Icons.Filled.Lock,
                contentDescription = if (badge.conquistada) "Conquistada" else "Ainda não conquistada",
                tint = if (badge.conquistada) Amber800 else InkMuted,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            badge.nome,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (badge.conquistada) Amber800 else InkMuted,
            textAlign = TextAlign.Center
        )
        Text(
            badge.descricao,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PreviaListas() {
    EduTrackTheme {
        Column(Modifier.padding(16.dp)) {
            ItemMissao(
                Missao("m1", "Faça 3 quizzes", TipoMissao.QUIZ, 150, 2, 3, concluida = false)
            )
            Spacer(Modifier.height(8.dp))
            ItemMissao(
                Missao("m2", "Check-in emocional", TipoMissao.CHECKIN, 50, 1, 1, concluida = true)
            )
            Spacer(Modifier.height(16.dp))
            Row {
                BadgeCircular(
                    Badge("b1", "Quiz Master", "+90% de acerto", conquistada = true),
                    Modifier.weight(1f)
                )
                BadgeCircular(
                    Badge("b2", "Mentor", "Ajudou 3 colegas", conquistada = false),
                    Modifier.weight(1f)
                )
            }
        }
    }
}
