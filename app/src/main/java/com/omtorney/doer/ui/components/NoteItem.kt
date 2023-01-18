package com.omtorney.doer.ui.notes

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
import com.omtorney.doer.ui.theme.DoerTheme
import com.omtorney.doer.util.Constants
import com.omtorney.doer.util.NotePriority
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDateTime
import java.util.*

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
        background = note.priority.color,
        onSwipe = { onSwipeStart(note) }
    )

    SwipeableActionsBox(
        startActions = listOf(pin),
        endActions = listOf(delete),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )
        ) {
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(note.priority.color)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = note.text,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Clip, // TODO Ellipsis after .width(IntrinsicSize.Max)
                )
                note.text.lines().drop(1).forEach { textLine ->
                    Text(
                        text = textLine,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        maxLines = 10, // TODO add option
                        // overflow = TextOverflow.Clip, // TODO Ellipsis after .width(IntrinsicSize.Max)
                    )
                }
            }
        }
    }
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
    Surface(
        color = note.priority.color.copy(alpha = 0.2f),
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = MaterialTheme.colors.background.copy(alpha = 0.5f))
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onNoteClick() },
                    onLongClick = { onLongClick(note) }
                )
        ) {
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(note.priority.color)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = note.text,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Clip, // TODO Ellipsis after .width(IntrinsicSize.Max)
                )
                note.text.lines().drop(1).forEach { textLine ->
                    Text(
                        text = textLine,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        maxLines = 10, // TODO add option
                        // overflow = TextOverflow.Clip, // TODO Ellipsis after .width(IntrinsicSize.Max)
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.ic_round_push_pin),
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                contentDescription = "Pinned",
                modifier = Modifier.size(15.dp)
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
            PinnedNoteItem(Note(
                1,
                "Note text",
                NotePriority.High,
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
            NoteItem(Note(
                1,
                "Note text",
                NotePriority.High,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                false
            ), {}, {}, {}, {}
            )
        }
    }
}