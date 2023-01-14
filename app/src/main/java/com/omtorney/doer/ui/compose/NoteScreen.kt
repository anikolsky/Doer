package com.omtorney.doer.ui.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.ui.viewmodel.HomeViewModel
import com.omtorney.doer.model.Note

@Composable
fun NoteScreen(
    viewModel: HomeViewModel,
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedNote = viewModel.selectedNote.value
    var selectedNoteText by rememberSaveable { mutableStateOf(selectedNote?.noteText ?: "") }
    val accentColor by viewModel.accentColor.collectAsState()

    Scaffold(topBar = {
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
                    note = selectedNote,
                    viewModel = viewModel,
                    context = context,
                    onClickClose = onClickClose
                )
                SaveIconButton(
                    note = selectedNote,
                    viewModel = viewModel,
                    noteText = selectedNoteText,
                    onClickClose = onClickClose
                )
            }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
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
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )
            Text(
                text = "Note id: ${selectedNote?.id}",
                textAlign = TextAlign.Center,
                color = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun DeleteIconButton(
    note: Note?,
    viewModel: HomeViewModel,
    context: Context,
    onClickClose: () -> Unit
) {
    IconButton(
        onClick = {
            note?.let {
                viewModel.deleteNote(it)
                Toast.makeText(
                    context,
                    context.getString(R.string.deleted),
                    Toast.LENGTH_SHORT
                ).show()
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
    note: Note?,
    viewModel: HomeViewModel,
    noteText: String,
    onClickClose: () -> Unit
) {
    IconButton(
        onClick = {
            note?.let { note ->
                if (note.id == null)
                    viewModel.addNote(noteText)
                else
                    viewModel.editNote(noteText)
            } ?: run { viewModel.addNote(noteText) }
            onClickClose()
        },
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_save_alt),
            contentDescription = stringResource(R.string.save)
        )
    }
}