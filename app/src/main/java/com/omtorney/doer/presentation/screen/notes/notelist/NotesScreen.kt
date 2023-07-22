package com.omtorney.doer.presentation.screen.notes.notelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.presentation.MainState
import com.omtorney.doer.presentation.Screen
import com.omtorney.doer.presentation.components.BottomBar
import com.omtorney.doer.presentation.components.MoreButton
import com.omtorney.doer.presentation.components.ScreenName
import com.omtorney.doer.presentation.components.SettingsButton
import com.omtorney.doer.presentation.components.TopBar
import com.omtorney.doer.presentation.screen.notes.components.NoteItem
import com.omtorney.doer.presentation.screen.notes.components.OrderSection
import com.omtorney.doer.presentation.screen.notes.components.PinnedNoteItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    mainState: MainState,
    notesState: NotesState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar {
                ScreenName(
                    title = Screen.Notes.label,
                    accentColor = mainState.accentColor,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    MoreButton(
                        accentColor = mainState.accentColor,
                        onClick = { onEvent(NotesEvent.ToggleOrderSection) }
                    )
                    SettingsButton(
                        accentColor = mainState.accentColor,
                        onClick = onSettingsClick
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                accentColor = mainState.accentColor
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddNoteClick() },
                containerColor = Color(mainState.accentColor),
                contentColor = contentColorFor(backgroundColor = Color(mainState.accentColor)),
                shape = MaterialTheme.shapes.small,
                elevation = FloatingActionButtonDefaults.elevation(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
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
            AnimatedVisibility(
                visible = notesState.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    OrderSection(
                        noteOrder = notesState.noteOrder,
                        onOrderChange = { onEvent(NotesEvent.Order(it)) },
                        color = Color(mainState.secondaryColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
                    .testTag("NOTES_SCREEN_LAZY_COLUMN")
            ) {
                item { Spacer(modifier = Modifier.height(mainState.noteSeparatorSize.dp)) }
                /** Pinned list */
                items(
                    items = notesState.notes,
                    key = { it.hashCode() }
                ) { note ->
                    if (note.isPinned) {
                        PinnedNoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note.id) },
                            onLongClick = { onEvent(NotesEvent.Pin(note)) },
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (mainState.noteSeparatorState) {
                            Spacer(modifier = Modifier.height(mainState.noteSeparatorSize.dp))
                        }
                    }
                }
                /** Unpinned list */
                items(
                    items = notesState.notes,
                    key = { it.hashCode() + 1 }
                ) { note ->
                    val snackbarMessage = stringResource(R.string.deleted)
                    val snackbarActionLabel = stringResource(R.string.undo)

                    if (!note.isPinned) {
                        NoteItem(
                            note = note,
                            onNoteClick = { onNoteClick(note.id) },
                            onLongClick = { onEvent(NotesEvent.Pin(note)) },
                            onSwipeStart = { onEvent(NotesEvent.Pin(note)) },
                            onSwipeEnd = {
                                onEvent(NotesEvent.Delete(note))
//                                coroutineScope.launch {
//                                    val result = scaffoldState.snackbarHostState.showSnackbar(
//                                        message = snackbarMessage,
//                                        actionLabel = snackbarActionLabel
//                                    )
//                                    if (result == SnackbarResult.ActionPerformed) {
//                                        onEvent(NotesEvent.RestoreNote)
//                                    }
//                                }
                            },
                            modifier = Modifier
                                .animateItemPlacement()
                                .testTag("NOTE_ITEM")
                        )
                        if (mainState.noteSeparatorState) {
                            Spacer(modifier = Modifier.height(mainState.noteSeparatorSize.dp))
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
