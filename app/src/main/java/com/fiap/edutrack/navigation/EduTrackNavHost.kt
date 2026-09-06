package com.fiap.edutrack.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fiap.edutrack.ui.components.EduTrackBottomBar
import com.fiap.edutrack.ui.screens.aula.DetalheAulaScreen
import com.fiap.edutrack.ui.screens.checkin.CheckInScreen
import com.fiap.edutrack.ui.screens.conquistas.ConquistasScreen
import com.fiap.edutrack.ui.screens.conteudo.ConteudoScreen
import com.fiap.edutrack.ui.screens.home.HomeScreen
import com.fiap.edutrack.ui.screens.login.LoginScreen
import com.fiap.edutrack.ui.screens.onboarding.OnboardingScreen
import com.fiap.edutrack.ui.screens.perfil.PerfilScreen
import com.fiap.edutrack.ui.screens.quiz.QuizScreen
import com.fiap.edutrack.viewmodel.EduTrackViewModel
import com.fiap.edutrack.viewmodel.EduTrackViewModelFactory

private val rotasComBottomBar = setOf(Rotas.HOME, Rotas.CONTEUDO, Rotas.CONQUISTAS, Rotas.PERFIL)

/**
 * Grafo único de navegação do app, com uma instância de [EduTrackViewModel]
 * compartilhada entre todas as telas (login -> onboarding -> área logada).
 * O ViewModel é criado pela factory manual, não pelo construtor padrão.
 *
 * O Snackbar vive aqui, e não em cada tela: as mensagens são eventos do ViewModel, e
 * concentrá-las num host só garante que a confirmação apareça mesmo quando a ação leva o
 * aluno para outra tela.
 */
@Composable
fun EduTrackNavHost() {
    val navController: NavHostController = rememberNavController()
    val viewModel: EduTrackViewModel = viewModel(factory = EduTrackViewModelFactory.Fabrica)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.mensagens.collect { mensagem ->
            snackbarHostState.showSnackbar(mensagem)
        }
    }

    // Navegação entre as abas: usada tanto pela barra inferior quanto pelos atalhos da Home.
    val navegarParaAba: (String) -> Unit = { rota ->
        navController.navigate(rota) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (rotaAtual in rotasComBottomBar) {
                EduTrackBottomBar(
                    rotaAtual = rotaAtual ?: Rotas.HOME,
                    onNavegar = navegarParaAba
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Rotas.LOGIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Rotas.LOGIN) {
                LoginScreen(viewModel = viewModel) {
                    navController.navigate(Rotas.ONBOARDING) {
                        popUpTo(Rotas.LOGIN) { inclusive = true }
                    }
                }
            }
            composable(Rotas.ONBOARDING) {
                OnboardingScreen(viewModel = viewModel) {
                    navController.navigate(Rotas.HOME) {
                        popUpTo(Rotas.ONBOARDING) { inclusive = true }
                    }
                }
            }
            composable(Rotas.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    aoAbrirCheckIn = { navController.navigate(Rotas.CHECKIN) },
                    aoAbrirAula = { aulaId -> navController.navigate(Rotas.aulaComId(aulaId)) },
                    aoNavegar = navegarParaAba
                )
            }
            composable(Rotas.DETALHE_AULA) { entrada ->
                val aulaId = entrada.arguments?.getString("aulaId").orEmpty()
                DetalheAulaScreen(viewModel = viewModel, aulaId = aulaId) {
                    navController.popBackStack()
                }
            }
            composable(Rotas.CONTEUDO) {
                ConteudoScreen(viewModel = viewModel) { trilhaId ->
                    navController.navigate(Rotas.quizComTrilha(trilhaId))
                }
            }
            composable(Rotas.QUIZ) { entrada ->
                val trilhaId = entrada.arguments?.getString("trilhaId").orEmpty()
                QuizScreen(viewModel = viewModel, trilhaId = trilhaId) {
                    navController.popBackStack()
                }
            }
            composable(Rotas.CONQUISTAS) {
                ConquistasScreen(viewModel = viewModel)
            }
            composable(Rotas.CHECKIN) {
                CheckInScreen(viewModel = viewModel) {
                    navController.popBackStack()
                }
            }
            composable(Rotas.PERFIL) {
                PerfilScreen(viewModel = viewModel)
            }
        }
    }
}
