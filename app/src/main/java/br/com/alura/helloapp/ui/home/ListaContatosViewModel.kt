package br.com.alura.helloapp.ui.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.database.ContatoDao
import br.com.alura.helloapp.preferences.PreferencesKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ListaContatosViewModel @Inject constructor(
    private val contatoDao: ContatoDao,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaContatosUiState())
    val uiState: StateFlow<ListaContatosUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            carregaTodos()
        }
        _uiState.update { state ->
            state.copy(
                onTextoBuscaChange = {
                    _uiState.value = _uiState.value.copy(textoBusca = it)
                    runBlocking { buscarTodosComParametro(it) }
                }
            )
        }
    }

    private suspend fun carregaTodos() {
        val contatos = contatoDao.buscaTodos()
        contatos.collect { contatosLista ->
            _uiState.update {
                it.copy(
                    contatos = contatosLista
                )
            }
        }
    }

    fun exibeSearchBar() {
        _uiState.update {
            it.copy(
                exibeSearchBar = !it.exibeSearchBar
            )
        }
    }

    private suspend fun buscarTodosComParametro(textoBusca: String) {
        viewModelScope.launch {
            val contatos = contatoDao.buscaContatoPorParametro("$textoBusca%")
            contatos.collect { contatosLista ->
                _uiState.update {
                    it.copy(
                        contatos = contatosLista
                    )
                }
            }
        }
    }

    fun setExibirAlertDialog() {
        _uiState.update {
            it.copy(
                exibeDialog = !it.exibeDialog
            )
        }
    }

    suspend fun deslogar() {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.LOGADO] = false
        }
    }

}