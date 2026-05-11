package com.appproject.takapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appproject.takapp.data.Constants
import com.appproject.takapp.data.SessionStore
import com.appproject.takapp.data.UserStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class HomeUiState(
    val hasActiveGames: Boolean = false,
    val isLogoutRequested: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionStore: SessionStore by lazy {
        SessionStore(application)
    }

    private val userStore: UserStore by lazy {
        UserStore(application)
    }

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = checkForActiveGames()
            result.fold(
                onSuccess = { activeGames ->
                    if (activeGames.length() > 0) {
                        _uiState.update { it.copy(hasActiveGames = true) }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
            )
        }
    }

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

    private suspend fun checkForActiveGames(): Result<JSONArray> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionStore.sessionToken.first()
                val url = URL("${Constants.BASE_URL}/api/activegames")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.doOutput = true

                val body = JSONObject()
                body.put("token", token)

                connection.outputStream.use { it.write(body.toString().toByteArray()) }
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)
                    val activeGames = json.getJSONArray("activeGames")
                    Result.success(activeGames)
                } else {
                    val error = connection.errorStream.bufferedReader().readText()
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}