package com.omtorney.doer.ui.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.ui.viewmodel.SettingsViewModel
import com.omtorney.doer.ui.theme.DoerTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var colorMenuExpanded by remember { mutableStateOf(false) }
    val lineSeparatorState = viewModel.lineSeparatorState.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                backgroundColor = Color(accentColor),
                navigationIcon = {
                    IconButton(onClick = onClickClose) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.dismiss)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(8.dp)) {
                LineSeparatorSwitcher(
                    checked = lineSeparatorState.value,
                    onCheckedChange = { viewModel.setLineSeparatorState(it) },
                    color = accentColor
                )
                AccentColor(
                    color = accentColor,
                    expanded = colorMenuExpanded,
                    onClickColorButton = { colorMenuExpanded = true },
                    onClickDropdownMenuItem = { viewModel.setAccentColor(it) },
                    onDismissRequest = { colorMenuExpanded = false }
                )
            }
        }
    }
}

@Composable
fun LineSeparatorSwitcher(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    color: Long
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Line separator",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(Color(color))
        )
    }
}

@Composable
fun AccentColor(
    color: Long,
    expanded: Boolean,
    onClickColorButton: () -> Unit,
    onClickDropdownMenuItem: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Accent color",
            modifier = Modifier.weight(1f)
        )
        Box {
            Button(
                onClick = onClickColorButton,
                colors = ButtonDefaults.buttonColors(Color(color))
            ) {
                Text(text = "Color")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFF4D4D5A)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color(0xFF4D4D5A))
                ) {
                    Text(text = "Grey")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFFFFCF44)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color(0xFFFFCF44))
                ) {
                    Text(text = "Yellow")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFFFF6859)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color(0xFFFF6859))
                ) {
                    Text(text = "Orange")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFF045D56)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color(0xFF045D56))
                ) {
                    Text(text = "Dark Green")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFF173d96)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color(0xFF173d96))
                ) {
                    Text(text = "Dark Blue")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    DoerTheme {
        SettingsScreen(hiltViewModel(), {})
    }
}