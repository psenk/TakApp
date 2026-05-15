package com.appproject.takapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appproject.takapp.ui.theme.TakAppTheme

// TODO: WHO IS THE OPPONENT??

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateScreenContent(
        uiState = uiState
    )
}

@Composable
fun CreateScreenContent(
    uiState: CreateUiState
) {
    val boardSizeOptions = listOf("5", "6", "7", "8", "9", "10")
    var boardSizeSelectedOption by remember { mutableStateOf(boardSizeOptions[0]) }
    val capstoneAmountOptions = listOf("1", "2", "3")
    var capstoneAmountSelectedOption by remember { mutableStateOf(capstoneAmountOptions[0]) }
    val firstPlayerOptions = listOf("Myself", "Opponent", "Random")
    var firstPlayerSelectedOption by remember { mutableStateOf(firstPlayerOptions[2]) }
    val friendsListOptions = mutableListOf("Bot")
    var friendsListSelectedOption by remember { mutableStateOf(friendsListOptions[0]) }

    uiState.friendsList?.forEach { friend ->
        friendsListOptions.add(friend.second)
    }



    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 50.dp)
        ) {
            Text(
                text = "Create New Game",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            SettingDropdownRow(
                "Board Size",
                boardSizeOptions,
                boardSizeSelectedOption,
                { boardSizeSelectedOption = it })
            SettingDropdownRow(
                "Capstone Amount",
                capstoneAmountOptions,
                capstoneAmountSelectedOption,
                { capstoneAmountSelectedOption = it })
            SettingDropdownRow(
                "First Turn",
                firstPlayerOptions,
                firstPlayerSelectedOption,
                { firstPlayerSelectedOption = it })
            SettingDropdownRow(
                "Opponent",
                friendsListOptions,
                friendsListSelectedOption,
                { friendsListSelectedOption = it }
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDropdownRow(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = label,
            fontSize = 20.sp
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .weight(1f)
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {}, // TODO: FUNCTIONALITY
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    TakAppTheme {
        CreateScreenContent(
            uiState = CreateUiState(
                friendsList = listOf(
                    Pair(2, "test-user-2"),
                    Pair(3, "test-user-3")
                )
            )
        )
    }
}