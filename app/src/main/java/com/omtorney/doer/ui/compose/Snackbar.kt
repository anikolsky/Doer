package com.omtorney.doer.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omtorney.doer.ui.viewmodel.HomeViewModel

@Composable
fun SnackbarUndoDeleteNote(
    data: SnackbarData,
    viewModel: HomeViewModel
) {
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
}