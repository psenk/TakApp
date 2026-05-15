package com.appproject.takapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appproject.takapp.data.Constants
import com.appproject.takapp.data.SessionStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class CreateUiState(
    val friendsList: List<Pair<Int, String>>? = null,
    val errorMessage: String? = null
)

class CreateViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionStore: SessionStore by lazy {
        SessionStore(application)
    }

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = getFriendsList()
            result.fold(
                onSuccess = { friendsList ->
                    _uiState.update { it.copy(friendsList = friendsList) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
            )
        }
    }

    private suspend fun getFriendsList(): Result<List<Pair<Int, String>>> {
        return withContext(Dispatchers.IO) {
            try {
                val token = sessionStore.sessionToken.first()
                val url = URL("${Constants.BASE_URL}/api/friends")
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
                    val friendsListArray = json.getJSONArray("friendsList")
                    val friendsList = (0 until friendsListArray.length()).map { i ->
                        val friend = friendsListArray.getJSONObject(i)
                        Pair(friend.getInt("playerId"), friend.getString("playerName"))
                    }
                    Result.success(friendsList)
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