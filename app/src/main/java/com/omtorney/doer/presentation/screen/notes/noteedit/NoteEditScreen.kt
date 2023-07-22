package com.omtorney.doer.presentation.screen.notes.noteedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omtorney.doer.R
import com.omtorney.doer.data.model.note.NotePriority
import com.omtorney.doer.presentation.MainState
import com.omtorney.doer.presentation.components.BackButton
import com.omtorney.doer.presentation.components.TopBar
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

@Composable
fun NoteEditScreen(
    modifier: Modifier = Modifier,
    mainState: MainState,
    noteEditState: NoteEditState,
    onEvent: (NoteEditEvent) -> Unit,
    onClickClose: () -> Unit
) {
    var noteInfoExpanded by remember { mutableStateOf(false) }
    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val snackbarCoroutineScope = rememberCoroutineScope()

//    LaunchedEffect(key1 = true) {
//        snackbarCoroutineScope.launch {
//            viewModel.eventFlow.collectLatest { uiEvent ->
//                when (uiEvent) {
//                    is UiEvent.ShowSnackbar -> {
//                        scaffoldState.snackbarHostState.showSnackbar(message = uiEvent.message)
//                    }
//                    UiEvent.Save -> {
//                        scaffoldState.snackbarHostState.showSnackbar(message = "Saved")
//                    }
//                    UiEvent.HideSnackbar -> {
//                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
//                    }
//                }
//            }
//        }
//    }

    Scaffold(
        topBar = {
            TopBar {
                BackButton(onClick = onClickClose)
                /** Priority buttons */
                Row(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        radioOptions.forEach { notePriority ->
//                            val priorityIndex = NoteConverter().priorityToInt(notePriority)
                            RadioButton(
                                selected = notePriority.index == noteEditState.priority.index,
                                onClick = { onEvent(NoteEditEvent.SetPriority(notePriority)) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = notePriority.color,
                                    unselectedColor = notePriority.color
                                )
                            )
                        }
                    }
                }
                /** Pin button */
                IconButton(onClick = { onEvent(NoteEditEvent.Pin) }) {
                    Icon(
                        painter = if (noteEditState.isPinned)
                            painterResource(R.drawable.ic_round_push_pin)
                        else
                            painterResource(R.drawable.ic_outline_push_pin),
                        contentDescription = stringResource(R.string.pin),
                        tint = contentColorFor(backgroundColor = Color(mainState.accentColor))
                    )
                }
                /** Delete button */
                IconButton(
                    onClick = {
                        onEvent(NoteEditEvent.Delete(noteEditState.id!!))
//                        snackbarCoroutineScope.launch {
//                            scaffoldState.snackbarHostState.showSnackbar(
//                                message = "Deleted",
//                                duration = SnackbarDuration.Short
//                            )
//                        }
                        onClickClose()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_delete),
                        contentDescription = stringResource(R.string.delete),
                        tint = contentColorFor(backgroundColor = Color(mainState.accentColor))
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(NoteEditEvent.Save)
                    onClickClose()
                },
                containerColor = Color(mainState.accentColor),
                contentColor = contentColorFor(backgroundColor = Color(mainState.accentColor)),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_save_alt),
                    contentDescription = stringResource(R.string.save)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(mainState.accentColor),
                            Color(mainState.secondaryColor)
                        ),
                        startX = 0f,
                        endX = LocalContext.current.resources.displayMetrics.widthPixels.dp.value
                    )
                )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        BasicTextField(
                            value = noteEditState.title,
                            onValueChange = { onEvent(NoteEditEvent.EnteredTitle(it)) },
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        Divider()
                        BasicTextField(
                            value = noteEditState.content,
                            onValueChange = { onEvent(NoteEditEvent.EnteredContent(it)) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .fillMaxSize()
                                .testTag("NOTE_EDIT_TEXT_FIELD")
                        )
                        Box(modifier = Modifier.align(Alignment.End)) {
                            IconButton(onClick = { noteInfoExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = "NoteEdit info"
                                )
                            }
                            NoteInfoMenu(
                                noteEditState = noteEditState,
                                expanded = noteInfoExpanded,
                                onDismissRequest = { noteInfoExpanded = false },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteInfoMenu(
    noteEditState: NoteEditState,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm:ss", getDefault())
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column {
            Text(
                text = """
                    NoteEdit id: ${noteEditState.id}
                    Created at: ${sdf.format(noteEditState.createdAt)}
                    Modified at: ${sdf.format(noteEditState.modifiedAt)}
                """.trimIndent(),
                color = Color.Gray.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}
