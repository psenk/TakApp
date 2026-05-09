package com.appproject.takapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appproject.takapp.ui.theme.TakAppTheme

@Composable
fun HomeScreen(
    onLogoutSuccess: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLogoutRequested) {
        if (uiState.isLogoutRequested) {
            viewModel.onNavigationHandled()
            onLogoutSuccess()
        }
    }

    HomeScreenContent(
        onResumeGameClick = viewModel::onResumeGameClick,
        onCreateGameClick = viewModel::onCreateGameClick,
        onGameHistoryClick = viewModel::onGameHistoryClick,
        onSettingsClick = viewModel::onSettingsClick,
        onLogoutClick = viewModel::onLogoutClick
    )
}

@Composable
private fun HomeScreenContent(
    onResumeGameClick: () -> Unit,
    onCreateGameClick: () -> Unit,
    onGameHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 50.dp)
        ) {
            Text(
                text = "TakApp",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onResumeGameClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resume Game")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onCreateGameClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Game")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onGameHistoryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Game History")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSettingsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Settings")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TakAppTheme {
        HomeScreenContent(
            onResumeGameClick = {},
            onCreateGameClick = {},
            onGameHistoryClick = {},
            onSettingsClick = {},
            onLogoutClick = {}
        )
    }
}