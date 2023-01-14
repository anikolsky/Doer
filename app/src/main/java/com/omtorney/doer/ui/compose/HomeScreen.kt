package com.omtorney.doer.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.omtorney.doer.R
import com.omtorney.doer.model.Note
import com.omtorney.doer.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNoteClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notes by viewModel.allNotes.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val lineSeparatorState by viewModel.lineSeparatorState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                backgroundColor = Color(accentColor),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.resetSelectedNote()
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
        snackbarHost = {

            SnackbarHost(
                hostState = it,
                snackbar = { data ->
                    Snackbar(
                        action = {
                            data.actionLabel?.let { actionLabel ->
                                TextButton(onClick = { viewModel.undoDeleteNote() }) {
                                    Text(
                                        text = actionLabel,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        },
                        backgroundColor = MaterialTheme.colors.background,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = data.message,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                },
                modifier = modifier
            )
        }
    )
    { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(
                items = notes,
                key = { it.id ?: "" }
            ) { note ->
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart ||
                            it == DismissValue.DismissedToEnd
                        ) {
                            viewModel.deleteNote(note)
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@rememberDismissState true
                        }
                        return@rememberDismissState false
                    }
                )
                NoteItem(
                    note = note,
                    onNoteClick = {
                        viewModel.selectNote(note)
                        onNoteClick()
                    },
                    onLongClick = { /* TODO something */ },
                    dismissState = dismissState,
                    modifier = Modifier.animateItemPlacement()
                )
                if (lineSeparatorState) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(color = Color(accentColor).copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}