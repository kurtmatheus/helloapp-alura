package br.com.alura.helloapp.ui.login

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.preferences.PreferencesKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SessaoViewModel"

@HiltViewModel
class SessaoViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _uiState = MutableStateFlow(SessaoUiState())
    val uiState: StateFlow<SessaoUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect {
                if (it[PreferencesKey.USUARIO_ATUAL] == null) {
                    desloga()
                }
            }
        }
    }

    private suspend fun desloga() {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.LOGADO] = false
        }
        Log.i(TAG, "desloga: passou por aqui")
        _uiState.value = _uiState.value.copy(
            logado = false
        )
    }
}

data class SessaoUiState(val logado: Boolean = true)