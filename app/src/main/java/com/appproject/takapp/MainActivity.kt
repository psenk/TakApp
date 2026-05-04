package com.appproject.takapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    private val BASE_URL = "https://127.0.0.1:8787"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginScreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim() // TODO: HASH THIS PASSWORD

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val result = loginRequest(username, password)
                withContext(Dispatchers.Main) {
                    result.onSuccess { sessionToken ->
                        // TODO: save token
                        // TODO: navigate to next screen
                        Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    }
                    result.onFailure { error ->
                        Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString() // TODO: HASH THIS PASSWORD

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val result = registerRequest(username, password)
                withContext(Dispatchers.Main) {
                    result.onSuccess { sessionToken ->
                        // TODO: save token
                        // TODO: navigate to next screen
                        Toast.makeText(this@MainActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    }
                    result.onFailure { error ->
                        Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // TODO: expand this?  new screen?  double check pass?  more info?
        }

        forgotPasswordButton.setOnClickListener {
            // TODO: functionality
        }
    }

    private suspend fun loginRequest(username: String, password: String): Result<String> {
        return try {
            val url = URL("$BASE_URL/api/login")
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

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val sessionToken = JSONObject(response).getString("sessionToken")
                Result.success(sessionToken)
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
            val url = URL("$BASE_URL/api/register")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val body = JSONObject()
            body.put("name", username)
            body.put("auth", password)

            connection.outputStream.use { it.write(body.toString().toByteArray()) }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val sessionToken = JSONObject(response).getString("sessionToken")
                Result.success(sessionToken)
            } else {
                val error = connection.errorStream.bufferedReader().readText()
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}