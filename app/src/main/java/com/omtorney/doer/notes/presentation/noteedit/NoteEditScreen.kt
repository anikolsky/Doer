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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omtorney.doer.R
import com.omtorney.doer.notes.domain.model.NotePriorityConverter
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.core.presentation.components.UiEvent
import com.omtorney.doer.notes.util.NotePriority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale.*

@Composable
fun NoteEditScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(), // TODO move to NavHost
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsStateWithLifecycle()
    val secondaryColor by viewModel.secondaryColor.collectAsStateWithLifecycle()

    val state = viewModel.state.value
    var noteInfoExpanded by remember { mutableStateOf(false) }
    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

//    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = true) {
        snackbarCoroutineScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = event.message)
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

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    Scaffold(
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
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(accentColor),
                            Color(secondaryColor)
                        ),
                        start = Offset(
                            0f,
                            0f
                        ),
                        end = Offset(
                            with(LocalDensity.current) { 600.dp.toPx() },
                            with(LocalDensity.current) { 600.dp.toPx() })
                    )
                )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                TopBar(modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    BackButton(onClick = onClickClose)
                    /** Priority buttons */
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colors.surface.copy(alpha = 0.45f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            radioOptions.forEach { notePriority ->
                                val priorityIndex = NotePriorityConverter().toInt(notePriority)
                                RadioButton(
                                    selected = priorityIndex == state.priority,
                                    onClick = {
                                        viewModel.onEvent(
                                            NoteEditEvent.SetPriority(
                                                priorityIndex
                                            )
                                        )
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.87f)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 16.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        BasicTextField(
                            value = state.text,
                            onValueChange = { viewModel.onEvent(NoteEditEvent.EnteredText(it)) },
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .fillMaxSize()
                                .testTag("NOTE_EDIT_TEXT_FIELD")
//                                .focusRequester(focusRequester)
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
                    Priority: ${state.priority}
                    Note id: ${state.id}
                    Created at:
                    ${sdf.format(state.createdAt)}
                    Modified at:
                    ${sdf.format(state.modifiedAt)}
                """.trimIndent(),
                color = Color.Gray.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}