package com.omtorney.doer.notes.presentation.notelist

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.core.presentation.components.*
import com.omtorney.doer.notes.presentation.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel(), // TODO move to NavHost
    navController: NavController,
    accentColor: Long,
    secondaryColor: Long,
    lineSeparatorState: Boolean,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(color = accentColor) {
                ScreenName(
                    title = Screen.Notes.label,
                    accentColor = accentColor,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    MoreButton(
                        accentColor = accentColor,
                        onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }
                    )
                    SettingsButton(
                        accentColor = accentColor,
                        onClick = onSettingsClick
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                accentColor = accentColor
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddNoteClick() },
                backgroundColor = Color(accentColor),
                contentColor = contentColorFor(backgroundColor = Color(accentColor))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
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
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    OrderSection(
                        noteOrder = state.noteOrder,
                        onOrderChange = { viewModel.onEvent(NotesEvent.Order(it)) },
                        color = Color(secondaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp)
                    .fillMaxSize()
                    .testTag("NOTES_SCREEN_LAZY_COLUMN")
            ) {
                /** Pinned list */
                items(
                    items = state.notes,
                    key = { it.id?.plus(1000) ?: "" } // TODO get other unique id
                ) { note ->
                    if (note.isPinned) {
                        PinnedNoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note.id!!) },
                            onLongClick = { viewModel.onEvent(NotesEvent.Pin(note)) },
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (lineSeparatorState) {
                            Spacer(modifier = Modifier.height(8.dp)) // TODO add an option
                        }
                    }
                }
                /** Unpinned list */
                items(
                    items = state.notes,
                    key = { it.id ?: "" }
                ) { note ->
                    val snackbarMessage = stringResource(R.string.deleted)
                    val snackbarActionLabel = stringResource(R.string.undo)

                    if (!note.isPinned) {
                        NoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note.id!!) },
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
                            modifier = Modifier
                                .animateItemPlacement()
                                .testTag("NOTE_ITEM")
                        )
                        if (lineSeparatorState) {
                            Spacer(modifier = Modifier.height(8.dp)) // TODO add an option
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(55.dp))
                }
            }
        }
    }
}
