package com.omtorney.doer.ui.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.model.Note
import com.omtorney.doer.presentation.HomeViewModel
import com.omtorney.doer.ui.theme.DoerTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNoteClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    val notes by viewModel.allNotes.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text(text = stringResource(R.string.app_name)) }) })
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NoteList(
                notes = notes,
                onNoteClick = { note ->
                    viewModel.selectNote(note)
                    onNoteClick()
                },
                onLongClick = { /* TODO something */ }
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        viewModel.resetSelectedNote()
                        onAddNoteClick()
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = stringResource(R.string.add))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = 8.dp,
                vertical = 0.dp
            ),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        notes.map { note ->
            item {
                NoteItem(
                    noteText = note.noteText,
                    modifier = Modifier.combinedClickable(
                        onClick = { onNoteClick(note) },
                        onLongClick = { /* onLongClick(note) */ }
                    ),
                    checked = false,
                    onCheckedChange = {}
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    noteText: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = noteText,
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(color = Color.Gray.copy(alpha = 0.3f))
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun NoteListPreview() {
    DoerTheme {
        Surface {
            NoteList(
                listOf(
                    Note(id = 1L, "Fake note 1"),
                    Note(id = 2L, "Fake note 2"),
                    Note(id = 3L, "Fake note 3")
                ),
                onNoteClick = {},
                onLongClick = {}
            )
        }
    }
}