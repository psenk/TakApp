package com.appproject.takapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appproject.takapp.data.SessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLogoutRequested: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionStore: SessionStore by lazy {
        SessionStore(application)
    }

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onResumeGameClick() {

    }

    fun onCreateGameClick() {

    }

    fun onGameHistoryClick() {

    }

    fun onSettingsClick() {

    }

    fun onLogoutClick() {
        viewModelScope.launch {
            sessionStore.clearSession()
            _uiState.update { it.copy(isLogoutRequested = true) }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(isLogoutRequested = false) }
    }
}