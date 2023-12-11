package br.com.alura.helloapp.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.alura.helloapp.navigation.DestinosHelloApp
import br.com.alura.helloapp.navigation.DetalhesContato
import br.com.alura.helloapp.navigation.FormularioContato
import br.com.alura.helloapp.navigation.FormularioUsuario
import br.com.alura.helloapp.navigation.ListaUsuarios
import br.com.alura.helloapp.navigation.buscaContatosGraph
import br.com.alura.helloapp.navigation.detalhesContatoGraph
import br.com.alura.helloapp.navigation.formularioContatoGraph
import br.com.alura.helloapp.navigation.homeGraph
import br.com.alura.helloapp.navigation.loginGraph
import br.com.alura.helloapp.navigation.splashGraph
import br.com.alura.helloapp.navigation.usuariosGraph
import br.com.alura.helloapp.ui.login.SessaoViewModel

private const val TAG = "NavHost"

@Composable
fun HelloAppNavHost(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinosHelloApp.SplashScreen.rota,
        modifier = modifier
    ) {
        splashGraph(
            onNavegaParaLogin = {
                navController.navegaParaLoginElimpaBackStack()
            },
            onNavegaParaHome = {
                navController.navegaParaHome()
            },
        )
        loginGraph(
            onNavegaParaHome = {
                navController.navegaParaHome()
            },
            onNavegaParaFormularioLogin = {
                navController.navegaParaFormlarioLogin()
            },
            onNavegaParaLogin = {
                navController.navegaParaLoginElimpaBackStack()
            },
        )
        homeGraph(
            onNavegaParaDetalhes = { idContato ->
                navController.navegaParaDetalhes(idContato)
            },
            onNavegaParaFormularioContato = {
                navController.navegaParaFormularioContato()
            },
            onNavegaParaDialgoUsuarios = { idUsuario ->
                navController.navegaParaDialgoUsuarios(idUsuario)
            },
            onNavegaParaBuscaContatos = {
                navController.navegaParaBuscaContatos()
            }
        )
        formularioContatoGraph(
            onVolta = {
                navController.popBackStack()
            },
        )
        detalhesContatoGraph(
            onVolta = {
                navController.popBackStack()
            },
            onNavegaParaDialgoUsuarios = { idContato ->
                navController.navegaParaFormularioContato(idContato)
            },
        )
        usuariosGraph(
            onVolta = {
                navController.popBackStack()
            },
            onNavegaParaLogin = {
                navController.navegaParaFormlarioLogin()
            },
            onNavegaParaHome = {
                navController.navegaParaHome()
            },
            onNavegaGerenciaUsuarios = {
                navController.navegaParaGerenciaUsuarios()
            },
            onNavegaParaFormularioUsuario = { idUsuario ->
                navController.navegaParaFormularioUsuario(idUsuario)
            },
        )
        buscaContatosGraph(
            onVolta = {
                navController.popBackStack()
            },
            onClickNavegaParaDetalhesContato = { idContato ->
                navController.navegaParaDetalhes(idContato)
            },
        )
    }

    val sessaoViewModel = hiltViewModel<SessaoViewModel>()
    val sessaoState = sessaoViewModel.uiState.collectAsState()

    if (!sessaoState.value.logado) {
        Log.i(TAG, "desloga: passou por aqui")
        navController.navegaParaLoginElimpaBackStack()
    }
}


fun NavHostController.navegaDireto(rota: String) = this.navigate(rota) {
    popUpTo(this@navegaDireto.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavHostController.navegaLimpo(rota: String) = this.navigate(rota) {
    popUpTo(0)
}

fun NavHostController.navegaParaDetalhes(idContato: Long) {
    navegaDireto("${DetalhesContato.rota}/$idContato")
}

fun NavHostController.navegaParaFormularioContato(idContato: Long = 0L) {
    navigate("${FormularioContato.rota}/$idContato")
}

fun NavHostController.navegaParaLoginElimpaBackStack() {
    navegaLimpo(DestinosHelloApp.LoginGraph.rota)
}

fun NavHostController.navegaParaDialgoUsuarios(idUsuario: String) {
    navigate("${ListaUsuarios.rota}/$idUsuario")
}

fun NavHostController.navegaParaFormularioUsuario(idUsuario: String) {
    navigate("${FormularioUsuario.rota}/$idUsuario")
}

fun NavHostController.navegaParaLogin() {
    navigate(DestinosHelloApp.Login.rota)
}

fun NavHostController.navegaParaHome() {
    navegaLimpo(DestinosHelloApp.HomeGraph.rota)
}

fun NavHostController.navegaParaFormlarioLogin() {
    navigate(DestinosHelloApp.FormularioLogin.rota)
}

fun NavHostController.navegaParaGerenciaUsuarios() {
    navigate(DestinosHelloApp.GerenciaUsuarios.rota)
}

fun NavHostController.navegaParaBuscaContatos() {
    navigate(DestinosHelloApp.BuscaContatos.rota)
}
