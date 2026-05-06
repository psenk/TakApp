package com.appproject.takapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appproject.takapp.ui.theme.TakAppTheme

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("TAKAPP")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TakAppTheme {
        HomeScreen()
    }
}