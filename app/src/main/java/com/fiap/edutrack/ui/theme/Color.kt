package com.fiap.edutrack.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Verde institucional - base de confiança (design system EduTrack)
val Green900 = Color(0xFF14532D)
val Green800 = Color(0xFF166534)
val Green600 = Color(0xFF16A34A)
val Green100 = Color(0xFFDCFCE7)

// Gradiente da marca: lime -> teal -> blue (momentos de energia/gamificação)
val BrandLime = Color(0xFF84CC16)
val BrandTeal = Color(0xFF0D9488)
val BrandBlue = Color(0xFF2563EB)
val BrandDeep = Color(0xFF0F172A)

// Reservados para conquista e risco
val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Amber100 = Color(0xFFFEF3C7)
val Amber800 = Color(0xFF92400E)
val Red500 = Color(0xFFEF4444)

// Neutros
val Ink50 = Color(0xFFF8FAFC)
val InkMuted = Color(0xFF64748B)
val Slate100 = Color(0xFFF1F5F9)

/** Borda sutil dos cartões — preto a 8% de opacidade. */
val BordaSutil = Color(0x14000000)

// Retorno de resposta no quiz
val VerdeAcerto = Color(0xFFBBF7D0)
val VermelhoErro = Color(0xFFFECACA)

/** Gradiente institucional, usado nos blocos de cabeçalho e nos selos da marca. */
val GradienteMarca = Brush.linearGradient(listOf(Green900, Green800, BrandTeal))
