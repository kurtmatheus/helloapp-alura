package br.com.alura.helloapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.alura.helloapp.R
import br.com.alura.helloapp.ui.home.ListaContatosUiState

@Composable
fun SearchBar(
    listaContatosUiState: ListaContatosUiState
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = listaContatosUiState.textoBusca,
        onValueChange = listaContatosUiState.onTextoBuscaChange,
        placeholder = { Text(text = stringResource(id = R.string.placeholder_searchbar)) }
    )
}