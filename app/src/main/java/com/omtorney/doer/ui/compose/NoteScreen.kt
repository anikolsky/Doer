package com.omtorney.doer.ui.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.presentation.HomeViewModel

@Composable
fun NoteScreen(
    viewModel: HomeViewModel,
    onClickClose: () -> Unit
) {
    val note = viewModel.selectedNote.value
    var noteText by rememberSaveable { mutableStateOf(note?.noteText ?: "") }
    val context = LocalContext.current

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
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            actions = {
                Button(
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
                    Text(text = stringResource(R.string.save))
                }
                Button(
                    onClick = {
                        note?.let { viewModel.deleteNote(it) }
                        Toast.makeText(
                            context,
                            context.getString(R.string.deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                        onClickClose()
                    }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = MaterialTheme.colors.background)
        ) {
            BasicTextField(
                value = noteText,
                onValueChange = { text ->
                    noteText = text
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )
            Text(
                text = "Note id: ${note?.id}",
                textAlign = TextAlign.Center,
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}