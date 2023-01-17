package com.omtorney.doer.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.ui.components.OrderSection
import com.omtorney.doer.ui.notes.NoteItem
import com.omtorney.doer.ui.notes.PinnedNoteItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNoteClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val lineDividerState by viewModel.lineDividerState.collectAsState()

    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    viewModel.resetSelectedNote()
                    onAddNoteClick()
                },
                backgroundColor = Color(accentColor)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        scaffoldState = scaffoldState,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.sort)
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = { viewModel.onEvent(NotesEvent.Order(it)) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = state.notes,
                    key = { it.id?.plus(1000) ?: "" } // TODO get other unique id
                ) { note ->
                    if (note.isPinned) {
                        PinnedNoteItem(
                            note = note,
                            onNoteClick = {
//                                viewModel.selectNote(note)
                                onNoteClick()
                            },
                            onLongClick = { viewModel.onEvent(NotesEvent.Pin(note)) }
                        )
                    }
                }
                items(
                    items = state.notes,
                    key = { it.id ?: "" }
                ) { note ->
                    val snackbarMessage = stringResource(R.string.deleted)
                    val snackbarActionLabel = stringResource(R.string.undo)

                    if (!note.isPinned) {
                        NoteItem(
                            note = note,
                            onNoteClick = {
//                                viewModel.selectNote(note)
                                onNoteClick()
                            },
                            onLongClick = { viewModel.onEvent(NotesEvent.Pin(note)) },
                            onSwipeStart = { viewModel.onEvent(NotesEvent.Pin(note)) },
                            onSwipeEnd = {
                                viewModel.onEvent(NotesEvent.Delete(note))
                                coroutineScope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = snackbarMessage,
                                        actionLabel = snackbarActionLabel
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(NotesEvent.RestoreNote)
                                    }
                                }
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (lineDividerState) {
                            Divider(
                                thickness = 0.8.dp,
                                color = Color(accentColor).copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}