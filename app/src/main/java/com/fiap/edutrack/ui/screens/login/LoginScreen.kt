package com.fiap.edutrack.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.edutrack.data.model.Instituto
import com.fiap.edutrack.ui.components.EduTrackButton
import com.fiap.edutrack.ui.components.IconeQuadrado
import com.fiap.edutrack.ui.theme.*
import com.fiap.edutrack.viewmodel.EduTrackViewModel

/**
 * T01 · Login multi-instituto.
 * O aluno entra escolhendo a instituição parceira (SENAI, SENAC, Alicerce, Eurofarma)
 * a partir de uma grade, sem formulário pesado — e é este login multi-instituto que
 * materializa na tela o diferencial "multi-parceiro nativo" do pitch.
 */
@Composable
fun LoginScreen(
    viewModel: EduTrackViewModel,
    aoEntrar: () -> Unit
) {
    val institutoSelecionado by viewModel.institutoSelecionado.collectAsStateWithLifecycle()

    // Estado de formulário: vive na tela, não no ViewModel. Não há autenticação real.
    var email by remember { mutableStateOf(viewModel.aluno.email) }
    var senha by remember { mutableStateOf("senha1234") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var lembrarDeMim by remember { mutableStateOf(true) }

    val podeEntrar = institutoSelecionado != null && email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        MarcaEduTrack(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(28.dp))
        Text(
            "ENTRAR COMO ALUNO DE",
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted
        )
        Spacer(Modifier.height(12.dp))

        // Quatro institutos em duas linhas simples: a grade cresce com a fonte do sistema.
        viewModel.institutos.chunked(2).forEach { linha ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                linha.forEach { instituto ->
                    CartaoInstituto(
                        instituto = instituto,
                        selecionado = institutoSelecionado?.id == instituto.id,
                        aoSelecionar = { viewModel.selecionarInstituto(instituto) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (linha.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        CampoComRotulo(rotulo = "E-mail ou matrícula") {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(14.dp))
        CampoComRotulo(rotulo = "Senha") {
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                singleLine = true,
                visualTransformation =
                    if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                            tint = InkMuted
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = lembrarDeMim,
                onCheckedChange = { lembrarDeMim = it },
                colors = CheckboxDefaults.colors(checkedColor = Green800)
            )
            Text("Lembrar de mim", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { }) {
                Text(
                    "Esqueci a senha",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Green800
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        EduTrackButton(texto = "Entrar", habilitado = podeEntrar, onClick = aoEntrar)
        if (!podeEntrar) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Escolha seu instituto para continuar.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = BordaSutil)
            Text(
                "OU",
                style = MaterialTheme.typography.labelMedium,
                color = InkMuted,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = BordaSutil)
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = aoEntrar,
            enabled = podeEntrar,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Green800),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
        ) {
            Icon(Icons.Filled.MailOutline, contentDescription = null, tint = Green800)
            Spacer(Modifier.width(8.dp))
            Text(
                "Entrar com e-mail institucional",
                color = Green800,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "Primeiro acesso? Use a senha enviada pelo seu instituto.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Símbolo em gradiente + palavra "EduTrack" em duas cores, como na referência. */
@Composable
private fun MarcaEduTrack(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GradienteMarca),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.School,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Green900)) { append("Edu") }
                withStyle(SpanStyle(color = BrandBlue)) { append("Track") }
            },
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Sua jornada de aprendizado, em um só lugar.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted,
            textAlign = TextAlign.Center
        )
    }
}

/** Rótulo acima do campo, em vez do rótulo flutuante padrão do Material. */
@Composable
private fun CampoComRotulo(rotulo: String, campo: @Composable () -> Unit) {
    Column {
        Text(
            rotulo,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        campo()
    }
}

/** Ícone de cada instituto — presentação, por isso mora aqui e não no modelo. */
private fun iconeDoInstituto(institutoId: String): ImageVector = when (institutoId) {
    "senai" -> Icons.Filled.Factory
    "senac" -> Icons.Filled.Storefront
    "alicerce" -> Icons.AutoMirrored.Filled.MenuBook
    else -> Icons.Filled.Science
}

@Composable
private fun CartaoInstituto(
    instituto: Instituto,
    selecionado: Boolean,
    aoSelecionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = aoSelecionar,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (selecionado) 2.dp else 1.dp,
            color = if (selecionado) Green800 else BordaSutil
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                IconeQuadrado(
                    icone = iconeDoInstituto(instituto.id),
                    corFundo = Green800,
                    corIcone = Color.White
                )
                Spacer(Modifier.weight(1f))
                if (selecionado) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Instituto selecionado",
                        tint = Green600,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                instituto.sigla,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Green900
            )
            Text(
                instituto.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    }
}
