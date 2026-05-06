package com.appproject.takapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel : ViewModel() {

    // TODO: swap to HTTPS before release
    private val baseUrl = "http://10.0.2.2:8787"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onLoginClick() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter username and password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginRequest(username, password)
            result.fold(
                onSuccess = { sessionToken ->
                    // TODO: persist session token (e.g. DataStore)
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun onRegisterClick() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter username and password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = registerRequest(username, password)
            result.fold(
                onSuccess = { sessionToken ->
                    // TODO: persist session token (e.g. DataStore)
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun onForgotPasswordClick() {
        // TODO: implement this
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }

    private suspend fun loginRequest(username: String, password: String): Result<String> {
        return try {
            val url = URL("$baseUrl/api/login")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val body = JSONObject()
            val credentials = JSONObject()
            credentials.put("name", username)
            credentials.put("auth", password)
            body.put("credentials", credentials)

            connection.outputStream.use { it.write(body.toString().toByteArray()) }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                Result.success(JSONObject(response).getString("sessionToken"))
            } else {
                val error = connection.errorStream.bufferedReader().readText()
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun registerRequest(username: String, password: String): Result<String> {
        return try {
            val url = URL("$baseUrl/api/register")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val body = JSONObject()
            body.put("name", username)
            body.put("auth", password)

            connection.outputStream.use { it.write(body.toString().toByteArray()) }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                Result.success(JSONObject(response).getString("sessionToken"))
            } else {
                val error = connection.errorStream.bufferedReader().readText()
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}