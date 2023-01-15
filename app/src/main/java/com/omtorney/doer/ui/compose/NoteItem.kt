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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omtorney.doer.R
import com.omtorney.doer.model.Note
import com.omtorney.doer.model.NotePriority
import com.omtorney.doer.ui.theme.DoerTheme
import java.time.LocalDateTime
import java.util.*

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
    val followingText = note.text.lines().drop(1)

    SwipeToDismiss(
        state = dismissState,
        dismissThresholds = { FractionalThreshold(0.20f) },
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(45.dp)
                    .background(note.priority.color)
                    .align(Alignment.Top)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = note.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                note.text.lines().drop(1).forEach {
                    Text(
                        text = it,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun NoteItemPreview() {
    DoerTheme {
        Surface {
            NoteItem(Note(
                1,
                "Note text",
                NotePriority.High,
                LocalDateTime.now(),
                LocalDateTime.now()
            ), {}, {}, rememberDismissState()
            )
        }
    }
}