package com.fiap.edutrack.data.model

enum class PerfilHolland(val rotulo: String) {
    REALISTA("Realista"),
    INVESTIGATIVO("Investigativo"),
    ARTISTICO("Artístico"),
    SOCIAL("Social"),
    EMPREENDEDOR("Empreendedor"),
    CONVENCIONAL("Convencional")
}

data class PerguntaOnboarding(
    val id: Int,
    val texto: String,
    val opcoes: List<OpcaoOnboarding>
)

data class OpcaoOnboarding(
    val letra: String,
    val texto: String,
    val perfil: PerfilHolland
)
