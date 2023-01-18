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
import com.omtorney.doer.util.NotePriority
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()

//    val state by viewModel.state

//    var enteredText by rememberSaveable { mutableStateOf(state.note.text) }
//    var selectedPriority by rememberSaveable { mutableStateOf(state.note.priority) }
//    var pinned by rememberSaveable { mutableStateOf(state.note.isPinned) }

    val enteredText by viewModel.noteText
    val selectedPriority by viewModel.priority
    val selectedPinned by viewModel.isPinned
    val selectedId by viewModel.id
    val selectedCreatedAt by viewModel.createdAt
    val selectedModifiedAt by viewModel.modifiedAt

    var changedText by rememberSaveable { mutableStateOf("") }
    var changedPriority by rememberSaveable { mutableStateOf<NotePriority>(NotePriority.No) }

    val radioOptions = listOf(
        NotePriority.High,
        NotePriority.Medium,
        NotePriority.Low,
        NotePriority.No
    )
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

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
                    /** Save button */
/*                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_save_alt),
                            contentDescription = stringResource(R.string.save)
                        )
                    }*/
                }
                BasicTextField(
                    value = enteredText.text,
                    onValueChange = { text ->
                        changedText = text
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
                            selected = option == selectedPriority.priority,
                            onClick = { changedPriority = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = option.color,
                                unselectedColor = option.color
                            )
                        )
                    }
                }
                Text(
                    text = "Priority: ${selectedPriority}",
                    color = selectedPriority.priority.color.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Note id: ${selectedId}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Created at ${selectedCreatedAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Modified at ${selectedModifiedAt}",
                    color = Color.Gray.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
            }
        }
    }
}