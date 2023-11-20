package br.com.alura.helloapp.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.alura.helloapp.DestinosHelloApp
import br.com.alura.helloapp.R
import br.com.alura.helloapp.ui.home.ListaContatosTela
import br.com.alura.helloapp.ui.home.ListaContatosViewModel
import br.com.alura.helloapp.ui.navegaParaDetalhes
import br.com.alura.helloapp.ui.navegaParaFormularioContato
import br.com.alura.helloapp.ui.navegaParaLoginDeslogado
import kotlinx.coroutines.launch

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = DestinosHelloApp.ListaContatos.rota,
        route = DestinosHelloApp.HomeGraph.rota,
    ) {
        composable(route = DestinosHelloApp.ListaContatos.rota) {
            val viewModel = hiltViewModel<ListaContatosViewModel>()
            val state by viewModel.uiState.collectAsState()

            val scope = rememberCoroutineScope()

            ListaContatosTela(
                state = state,
                onClickAbreDetalhes = { idContato ->
                    navController.navegaParaDetalhes(idContato)
                },
                onClickAbreCadastro = {
                    navController.navegaParaFormularioContato()
                },
                onClickDesloga = {
                    viewModel.setExibirAlertDialog()
                },
                onClickSearchIcon = {
                    viewModel.exibeSearchBar()
                })

            if (state.exibeDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setExibirAlertDialog() },
                    title = { Text(text = stringResource(id = R.string.atencao_deslogar)) },
                    buttons = {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = {
                                scope.launch {
                                    viewModel.deslogar()
                                }
                                navController.navegaParaLoginDeslogado()
                            }) {
                                Text(text = stringResource(id = R.string.deslogar))
                            }

                            TextButton(onClick = {
                                viewModel.setExibirAlertDialog()
                            }) {
                                Text(text = stringResource(id = R.string.cancelar))
                            }
                        }
                    }
                )
            }
        }
    }
}

