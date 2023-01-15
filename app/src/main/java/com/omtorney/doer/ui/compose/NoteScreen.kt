package com.omtorney.doer.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.model.Note
import com.omtorney.doer.model.NotePriority
import com.omtorney.doer.ui.viewmodel.HomeViewModel
import com.omtorney.doer.util.Constants
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    viewModel: HomeViewModel,
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val selectedNote = viewModel.selectedNote.value
    var selectedNoteText by rememberSaveable { mutableStateOf(selectedNote?.text ?: "") }
    var selectedNotePriority by remember {
        mutableStateOf(selectedNote?.priority ?: Constants.initialPriority)
    }
    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.edit)) },
                navigationIcon = {
                    IconButton(onClick = onClickClose) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.dismiss)
                        )
                    }
                },
                backgroundColor = Color(accentColor),
                actions = {
                    DeleteIconButton(
                        viewModel = viewModel,
                        note = selectedNote,
                        onClickClose = onClickClose,
                        onDelete = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                    SaveIconButton(
                        viewModel = viewModel,
                        note = selectedNote,
                        text = selectedNoteText,
                        priority = selectedNotePriority,
                        onClickClose = onClickClose
                    )
                }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                snackbar = { data ->
                    SnackbarUndoDeleteNote(
                        data = data,
                        viewModel = viewModel
                    )
                }
            )
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(8.dp)) {
                BasicTextField(
                    value = selectedNoteText,
                    onValueChange = { changedText ->
                        selectedNoteText = changedText
                    },
                    textStyle = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxSize()
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(accentColor).copy(alpha = 0.2f))
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    radioOptions.forEach { option ->
                        RadioButton(
                            selected = option == selectedNotePriority,
                            onClick = { selectedNotePriority = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = option.color,
                                unselectedColor = option.color
                            )
                        )
                    }
                }
                Text(
                    text = "Priority: ${selectedNotePriority.status}",
                    color = selectedNotePriority.color.copy(alpha = 0.3f)
                )
                Text(
                    text = "Note id: ${selectedNote?.id}",
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                Text(
                    text = "Created at ${selectedNote?.createdAt}",
                    color = Color.Gray.copy(alpha = 0.3f)
                )
                Text(
                    text = "Changed at ${selectedNote?.changedAt}",
                    color = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun DeleteIconButton(
    viewModel: HomeViewModel,
    note: Note?,
    onClickClose: () -> Unit,
    onDelete: () -> Unit
) {
    IconButton(
        onClick = {
            note?.let {
                viewModel.deleteNote(it)
                onDelete()
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

@Composable
fun SaveIconButton(
    viewModel: HomeViewModel,
    note: Note?,
    text: String,
    priority: NotePriority,
    onClickClose: () -> Unit
) {
    IconButton(
        onClick = {
            note?.let { note ->
                if (note.id == null)
                    viewModel.addNote(text = text, priority = priority)
                else
                    viewModel.editNote(text = text, priority = priority)
            } ?: run { viewModel.addNote(text = text, priority = priority) }
            onClickClose()
        },
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_save_alt),
            contentDescription = stringResource(R.string.save)
        )
    }
}