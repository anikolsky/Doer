package com.omtorney.doer.presentation.screen.notes.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.model.note.NoteConverter
import com.omtorney.doer.data.model.note.NotePriority
import com.omtorney.doer.data.model.note.NoteStatus
import com.omtorney.doer.presentation.theme.DoerTheme
import me.saket.swipe.SwipeAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onLongClick: (Note) -> Unit,
    onSwipeStart: (Note) -> Unit,
    onSwipeEnd: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val delete = SwipeAction(
        icon = painterResource(R.drawable.ic_round_delete),
        background = Color.Red,
        onSwipe = { onSwipeEnd(note) },
        isUndo = true
    )

    val pin = SwipeAction(
        icon = painterResource(R.drawable.ic_round_push_pin),
        background = Color.Blue,
        onSwipe = { onSwipeStart(note) }
    )

//    SwipeableActionsBox(
//        startActions = listOf(pin),
//        endActions = listOf(delete),
//        swipeThreshold = 60.dp,
//        backgroundUntilSwipeThreshold = Color.Transparent
//    ) {
        Card(
            shape = MaterialTheme.shapes.small,
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )
        ) {
            Row {
                Spacer(
                    modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(NoteConverter().priorityFromInt(note.priority.index).color)
                )
                Column(modifier = Modifier.padding(14.dp)) {
                    if (note.title.isNotEmpty()) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            maxLines = 2,
//                        overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 10, // TODO add option
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
//    }
}

// TODO common NoteItem:
//  1 - wrap with SwipeableActionsBox,
//  2 - pass modifier with background color (if needed)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinnedNoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onLongClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = Color.Red.copy(alpha = 0.1f))
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )
        ) {
            Spacer(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(NoteConverter().priorityFromInt(note.priority.index).color)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp)
            ) {
                if (note.title.isNotEmpty()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 10, // TODO add an option
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_round_push_pin),
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                contentDescription = "Pinned",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun NotePinnedItemPreview() {
    DoerTheme {
        Surface {
            PinnedNoteItem(
                Note(
                    1,
                    "Title",
                    "NoteEdit text note text\nnote text note text",
                    NotePriority.High,
                    NoteStatus.ToDo,
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    false
                ), {}, {}
            )
        }
    }
}

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
            NoteItem(
                Note(
                    1,
                    "Title",
                    "NoteEdit text note text\nnote text note text",
                    NotePriority.High,
                    NoteStatus.ToDo,
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    false
                ), {}, {}, {}, {}
            )
        }
    }
}
