package com.omtorney.doer.settings.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.settings.presentation.components.ColorPickerDialog
import com.omtorney.doer.core.presentation.theme.DoerTheme
import com.omtorney.doer.settings.presentation.components.ColorType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val context = LocalContext.current
    val lineDividerState = viewModel.lineDividerState.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val secondaryColor by viewModel.secondaryColor.collectAsState()
    var colorPickerOpen by remember { mutableStateOf(false) }
    var colorType by remember { mutableStateOf<ColorType>(ColorType.Accent) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {

        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onClickClose) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.dismiss)
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h5.merge(
                            TextStyle(fontWeight = FontWeight.Bold)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                LineDividerSwitcher(
                    color = accentColor,
                    checked = lineDividerState.value,
                    onCheckedChange = { viewModel.setLineDividerState(it) }
                )
                /** Accent color */
                SettingsMenuButton(
                    icon = R.drawable.round_color,
                    title = {
                        Canvas(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(10))
                                .border(
                                    1.dp,
                                    MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                    RoundedCornerShape(10)
                                )
                                .background(color = Color(accentColor))
                        ) {}
                    },
                    subtitle = "Select accent color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Accent
                    }
                )
                /** Secondary color */
                SettingsMenuButton(
                    icon = R.drawable.round_color,
                    title = {
                        Canvas(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(10))
                                .border(
                                    1.dp,
                                    MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                                    RoundedCornerShape(10)
                                )
                                .background(color = Color(secondaryColor))
                        ) {}
                    },
                    subtitle = "Select secondary color",
                    onClick = {
                        colorPickerOpen = true
                        colorType = ColorType.Secondary
                    }
                )
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f) // TODO add an option
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
        if (colorPickerOpen) {
            ColorPickerDialog(
                accentColor = accentColor,
                onChooseClick = { color ->
                    when (colorType) {
                        ColorType.Accent -> {
                            viewModel.setAccentColor(color)
                        }
                        ColorType.Secondary -> {
                            viewModel.setSecondaryColor(color)
                        }
                    }
                },
                onDismiss = { colorPickerOpen = false }
            )
        }
    }
}

@Composable
fun SettingsMenuButton(
    @DrawableRes icon: Int,
    title: @Composable () -> Unit,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = subtitle
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            title()
            Text(text = subtitle, style = MaterialTheme.typography.caption)
        }
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
        ) {
            Text(text = "Select")
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
        Text(
            text = "Database",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
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
fun LineDividerSwitcher(
    color: Long,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Divide notes",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(Color(color))
        )
    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    DoerTheme {
        SettingsScreen(Modifier, hiltViewModel(), {})
    }
}