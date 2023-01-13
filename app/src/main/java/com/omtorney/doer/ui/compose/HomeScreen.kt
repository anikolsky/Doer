package com.omtorney.doer.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.presentation.HomeViewModel

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNoteClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    val notes by viewModel.allNotes.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name)) },
            backgroundColor = MaterialTheme.colors.primary
        )
    })
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
                            return@rememberDismissState true
                        }
                        return@rememberDismissState false
                    }
                )
                NoteItem(
                    note = note,
                    onNoteClick = { noteItem ->
                        viewModel.selectNote(noteItem)
                        onNoteClick()
                    },
                    onLongClick = { /* TODO something */ },
                    dismissState = dismissState,
                    modifier = Modifier.animateItemPlacement()
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(color = MaterialTheme.colors.primary.copy(alpha = 0.1f))
                )
            }
            item(key = "add_button") {
                Box(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.resetSelectedNote()
                            onAddNoteClick()
                        },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 18.dp)
                            .height(50.dp)
                            .width(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_add_task),
                            contentDescription = stringResource(R.string.add)
                        )
                    }
                }
            }
        }
    }
}