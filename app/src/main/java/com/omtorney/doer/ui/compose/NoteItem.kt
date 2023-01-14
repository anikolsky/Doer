package com.omtorney.doer.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.model.Note
import com.omtorney.doer.ui.theme.DoerTheme

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onLongClick: (Note) -> Unit,
    dismissState: DismissState,
    modifier: Modifier = Modifier
) {
    SwipeToDismiss(
        state = dismissState,
        dismissThresholds = { FractionalThreshold(0.25f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val iconTint = when (dismissState.targetValue) {
                DismissValue.Default -> Color.Gray
                DismissValue.DismissedToStart -> Color.White
                DismissValue.DismissedToEnd -> Color.White
            }
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.Gray
                    DismissValue.DismissedToStart -> Color.Red
                    DismissValue.DismissedToEnd -> Color.Red
                }
            )
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = color)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_delete),
                    contentDescription = stringResource(R.string.delete),
                    tint = iconTint
                )
            }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )

        ) {
            Text(
                text = note.noteText,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, widthDp = 320)
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun NoteItemPreview() {
    DoerTheme {
        Surface {
            NoteItem(Note(1, "Note text"), {}, {}, rememberDismissState())
        }
    }
}