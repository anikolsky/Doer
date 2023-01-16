package com.omtorney.doer.ui.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.ui.theme.DoerTheme
import com.omtorney.doer.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                LineSeparatorSwitcher(
                    color = accentColor,
                    checked = lineSeparatorState.value,
                    onCheckedChange = { viewModel.setLineSeparatorState(it) }
                )
                AccentColor(
                    color = accentColor,
                    expanded = colorMenuExpanded,
                    onClickColorButton = { colorMenuExpanded = true },
                    onClickDropdownMenuItem = { viewModel.setAccentColor(it) },
                    onDismissRequest = { colorMenuExpanded = false }
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(accentColor).copy(alpha = 0.5f))
                )
                DatabaseActions(
                    color = accentColor,
                    onExportClick = {
                        viewModel.backupDatabase()
                        Toast.makeText(context, "Exported", Toast.LENGTH_SHORT).show()
                    },
                    onImportClick = { viewModel.restoreDatabase() }
                )
            }
        }
    }
}

@Composable
fun DatabaseActions(
    color: Long,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Database", modifier = Modifier.weight(1f))
        Button(
            onClick = onExportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Export")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onImportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Import")
        }
    }
}

@Composable
fun LineSeparatorSwitcher(
    color: Long,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
                    modifier = Modifier.background(Color(0xFF33333D))
                ) {
                    Text(text = "Grey")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFFFFCF44)
                        onDismissRequest()
                    },
                    modifier = Modifier.background(Color(0xFFFFCF44))
                ) {
                    Text(text = "Yellow")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFFFF6859)
                        onDismissRequest()
                    },
                    modifier = Modifier.background(Color(0xFFFF6859))
                ) {
                    Text(text = "Orange")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFF045D56)
                        onDismissRequest()
                    },
                    modifier = Modifier.background(Color(0xFF045D56))
                ) {
                    Text(text = "Dark Green")
                }
                DropdownMenuItem(
                    onClick = {
                        onClickDropdownMenuItem(0xFF173d96)
                        onDismissRequest()
                    },
                    modifier = Modifier.background(Color(0xFF173d96))
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