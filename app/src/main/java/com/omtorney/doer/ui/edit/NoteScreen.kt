package com.omtorney.doer.ui.edit

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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.ui.home.NotesEvent
import com.omtorney.doer.util.Constants
import com.omtorney.doer.util.NotePriority
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val selectedNote = viewModel.selectedNote.value
    var selectedNoteText by rememberSaveable { mutableStateOf(selectedNote?.text ?: "") }
    var selectedNotePinned by rememberSaveable { mutableStateOf(selectedNote?.isPinned) }
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
                            selectedNote?.let {
                                viewModel.pinNote(it)
                                selectedNotePinned = !selectedNotePinned!!
                            }
                        }
                    ) {
                        Icon(
                            painter = if (selectedNotePinned == true)
                                painterResource(R.drawable.ic_round_push_pin)
                            else
                                painterResource(R.drawable.ic_outline_push_pin),
                            contentDescription = stringResource(R.string.pin)
                        )
                    }
                    /** Delete button */
                    IconButton(
                        onClick = {
                            selectedNote?.let {
                                viewModel.onEvent(NotesEvent.Delete(it))
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                            onClickClose()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_delete_outline),
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                    /** Save button */
                    IconButton(
                        onClick = {
                            selectedNote?.let { note ->
                                if (note.id == null)
                                    viewModel.addNote(
                                        text = selectedNoteText,
                                        priority = selectedNotePriority
                                    )
                                else
                                    viewModel.editNote(
                                        text = selectedNoteText,
                                        priority = selectedNotePriority
                                    )
                            } ?: run {
                                viewModel.addNote(
                                    text = selectedNoteText,
                                    priority = selectedNotePriority
                                )
                            }
                            onClickClose()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_save_alt),
                            contentDescription = stringResource(R.string.save)
                        )
                    }
                }
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
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color(accentColor).copy(alpha = 0.3f)
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
                    color = selectedNotePriority.color.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Note id: ${selectedNote?.id}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Created at ${selectedNote?.createdAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Changed at ${selectedNote?.modifiedAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
            }
        }
    }
}