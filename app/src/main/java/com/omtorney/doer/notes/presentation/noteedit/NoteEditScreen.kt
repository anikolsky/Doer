package com.omtorney.doer.notes.presentation.noteedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.core.presentation.components.UiEvent
import com.omtorney.doer.notes.domain.model.NoteConverters
import com.omtorney.doer.notes.domain.model.NotePriority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

@Composable
fun NoteEditScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(), // TODO move to NavHost
    accentColor: Long,
    secondaryColor: Long,
    onClickClose: () -> Unit
) {
    val state by viewModel.state
    var noteInfoExpanded by remember { mutableStateOf(false) }
    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        snackbarCoroutineScope.launch {
            viewModel.eventFlow.collectLatest { uiEvent ->
                when (uiEvent) {
                    is UiEvent.ShowSnackbar -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = uiEvent.message)
                    }
                    UiEvent.Save -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = "Saved")
                    }
                    UiEvent.HideSnackbar -> {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(color = accentColor) {
                BackButton(onClick = onClickClose)
                /** Priority buttons */
                Row(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        radioOptions.forEach { notePriority ->
                            val priorityIndex = NoteConverters().priorityToInt(notePriority)
                            RadioButton(
                                selected = priorityIndex == state.priority,
                                onClick = {
                                    viewModel.onEvent(NoteEditEvent.SetPriority(priorityIndex))
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = notePriority.color,
                                    unselectedColor = notePriority.color
                                )
                            )
                        }
                    }
                }
                /** Pin button */
                IconButton(onClick = { viewModel.onEvent(NoteEditEvent.Pin) }) {
                    Icon(
                        painter = if (state.isPinned)
                            painterResource(R.drawable.ic_round_push_pin)
                        else
                            painterResource(R.drawable.ic_outline_push_pin),
                        contentDescription = stringResource(R.string.pin),
                        tint = contentColorFor(backgroundColor = Color(accentColor))
                    )
                }
                /** Delete button */
                IconButton(
                    onClick = {
                        viewModel.onEvent(NoteEditEvent.Delete(state.id!!))
                        snackbarCoroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Deleted",
                                duration = SnackbarDuration.Short
                            )
                        }
                        onClickClose()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_delete),
                        contentDescription = stringResource(R.string.delete),
                        tint = contentColorFor(backgroundColor = Color(accentColor))
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(NoteEditEvent.Save)
                    onClickClose()
                },
                backgroundColor = Color(accentColor)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_save_alt),
                    contentDescription = stringResource(R.string.save)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        scaffoldState = scaffoldState,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(accentColor),
                            Color(secondaryColor)
                        ),
                        startX = 0f,
                        endX = LocalContext.current.resources.displayMetrics.widthPixels.dp.value
                    )
                )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        BasicTextField(
                            value = state.title,
                            onValueChange = { viewModel.onEvent(NoteEditEvent.EnteredTitle(it)) },
                            textStyle = MaterialTheme.typography.h6.copy(
                                color = MaterialTheme.colors.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        Divider()
                        BasicTextField(
                            value = state.content,
                            onValueChange = { viewModel.onEvent(NoteEditEvent.EnteredContent(it)) },
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
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
                                    contentDescription = "Note info"
                                )
                            }
                            NoteInfoMenu(
                                state = state,
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
    state: NoteEditState,
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
                    Note id: ${state.id}
                    Created at: ${sdf.format(state.createdAt)}
                    Modified at: ${sdf.format(state.modifiedAt)}
                """.trimIndent(),
                color = Color.Gray.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}