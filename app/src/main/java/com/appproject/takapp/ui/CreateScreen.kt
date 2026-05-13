package com.appproject.takapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.appproject.takapp.ui.theme.TakAppTheme

@Composable
fun CreateScreen() {

    //val boardSize = 5
    CreateScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreenContent() {

    Scaffold { innerPadding ->
        Text(
            text = "Create New Game",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 100.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp, vertical = 50.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(
                            align = Alignment.CenterVertically
                        ),
                    text = "Board Size",
                    fontSize = 20.sp,
                )

                Text(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(
                            align = Alignment.CenterVertically
                        ),
                    text = "Capstone Amount",
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 28.dp, vertical = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    val options = listOf("5", "6", "7", "8", "9", "10")
                    var expanded by remember { mutableStateOf(false) }
                    val textFieldState = rememberTextFieldState(options[0])
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier
                            .height(80.dp)
                            .wrapContentHeight(
                                align = Alignment.CenterVertically
                            )
                    ) {
                        TextField(
                            readOnly = true,
                            state = textFieldState,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        textFieldState.setTextAndPlaceCursorAtEnd(option)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    TakAppTheme {
        CreateScreen()
    }
}