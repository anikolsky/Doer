package com.omtorney.doer.notes.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.core.model.NotePriorityConverter
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.notes.util.NotePriority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale.*

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val secondaryColor by viewModel.secondaryColor.collectAsState()
    val state = viewModel.state.value
    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

//    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                NoteEditViewModel.UiEvent.SaveNote -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Saved")
                }
                is NoteEditViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
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
                TopBar(modifier = Modifier.padding(bottom = 8.dp)) {
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
                            coroutineScope.launch {
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
                    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm:ss", getDefault())

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
//                                .focusRequester(focusRequester)
                        )
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f) // TODO add an option
                        )
                        Text(
                            text = "Priority: ${state.priority}",
                            color = Color.Gray.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Note id: ${state.id}",
                            color = Color.Gray.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Created at ${sdf.format(state.createdAt)}",
                            color = Color.Gray.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Modified at ${sdf.format(state.modifiedAt)}",
                            color = Color.Gray.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}