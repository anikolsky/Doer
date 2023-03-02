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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
fun HomeScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val secondaryColor by viewModel.secondaryColor.collectAsState()
    val lineDividerState by viewModel.lineDividerState.collectAsState()

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
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
//                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(accentColor),
                            Color(secondaryColor)
                        ),
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(
                            with(LocalDensity.current) { 600.dp.toPx() },
                            with(LocalDensity.current) { 600.dp.toPx() })
                    )
                )
        ) {
            TopBar(modifier = Modifier.padding(8.dp)) {
                AppName(
                    accentColor = accentColor,
//                    modifier = Modifier.weight(1f)
                )
                ScreenName(
                    title = Screen.Notes.label,
                    accentColor = accentColor,
                    modifier = Modifier // TODO center
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
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    OrderSection(
                        noteOrder = state.noteOrder,
                        onOrderChange = { viewModel.onEvent(NotesEvent.Order(it)) },
                        color = Color(accentColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
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
                        if (lineDividerState) {
                            Spacer(modifier = Modifier.height(5.dp)) // TODO add an option
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
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (lineDividerState) {
                            Spacer(modifier = Modifier.height(5.dp)) // TODO add an option
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
