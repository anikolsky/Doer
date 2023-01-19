package com.omtorney.doer.notes.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.core.model.NotePriorityConverter
import com.omtorney.doer.notes.util.NotePriority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()

    val enteredText by viewModel.noteText
    val selectedPriority by viewModel.priority
    val selectedPinned by viewModel.isPinned
    val selectedId by viewModel.id
    val selectedCreatedAt by viewModel.createdAt
    val selectedModifiedAt by viewModel.modifiedAt

    val radioOptions = listOf(
//        4, 3, 2, 1
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                NoteEditViewModel.UiEvent.SaveNote -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Saved"
                    )
                }
                is NoteEditViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    /** Back button */
                    IconButton(onClick = onClickClose) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.dismiss)
                        )
                    }
                    /** App name */
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f)
                    )
                    /** Pin button */
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NoteEditEvent.Pin(isPinned = !selectedPinned.isPinned))
                        }
                    ) {
                        Icon(
                            painter = if (selectedPinned.isPinned)
                                painterResource(R.drawable.ic_round_push_pin)
                            else
                                painterResource(R.drawable.ic_outline_push_pin),
                            contentDescription = stringResource(R.string.pin)
                        )
                    }
                    /** Delete button */
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NoteEditEvent.Delete(selectedId.id!!))
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            onClickClose()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_delete_outline),
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
                BasicTextField(
                    value = enteredText.text,
                    onValueChange = { viewModel.onEvent(NoteEditEvent.EnteredText(it)) },
                    textStyle = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                )
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f) // TODO add an option
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    radioOptions.forEach { notePriority ->
                        val priorityIndex = NotePriorityConverter().toInt(notePriority)
                        RadioButton(
                            selected = priorityIndex == selectedPriority.priority,
                            onClick = { viewModel.onEvent(NoteEditEvent.SetPriority(priorityIndex)) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = notePriority.color,
                                unselectedColor = notePriority.color
                            )
                        )
                    }
                }
                Text(
                    text = "Priority: ${selectedPriority.priority}",
                    fontSize = 12.sp
                )
                Text(
                    text = "Note id: ${selectedId.id}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Created at ${selectedCreatedAt.createdAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Modified at ${selectedModifiedAt.modifiedAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
            }
        }
    }
}