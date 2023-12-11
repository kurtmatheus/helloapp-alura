package br.com.alura.helloapp.ui.userDialog

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.database.UsuarioDao
import br.com.alura.helloapp.preferences.PreferencesKey
import br.com.alura.helloapp.util.ID_USUARIO_ATUAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  ListaUsuariosViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore<Preferences>,
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    private val usuarioAtual = savedStateHandle.get<String>(ID_USUARIO_ATUAL)

    private val _uiState = MutableStateFlow(ListaUsuariosUiState())
    val uiState: StateFlow<ListaUsuariosUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            carregaDados()
        }
    }

    private suspend fun carregaDados() {
        usuarioAtual?.let { idUsuarioAtual ->
            val usuarioCarregado = usuarioDao.buscaPorId(idUsuarioAtual).first()

            usuarioCarregado?.let {
                _uiState.update {
                    it.copy(
                        nomeDeUsuario = usuarioCarregado.idUsuario,
                        nome = usuarioCarregado.nome
                    )
                }
            }
        }

        usuarioDao.buscarTodos().collect { usuarioList ->
            val usuariosFiltrados = usuarioList.filter { it.idUsuario != usuarioAtual }

            _uiState.update {
                it.copy(
                    outrasContas = usuariosFiltrados
                )
            }
        }
    }

    suspend fun atualizaUsuarioLogado(novoUsuario: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.USUARIO_ATUAL] = novoUsuario
        }
    }
}