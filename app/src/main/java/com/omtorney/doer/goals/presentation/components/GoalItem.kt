package com.omtorney.doer.goals.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NoteConverters
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalItem(
    goal: Goal,
    progress: Float,
    color: Long,
    onGoalClick: () -> Unit,
    onLongClick: (Goal) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onGoalClick() },
                onLongClick = { onLongClick(goal) }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = goal.title,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            CircularProgressIndicator(
                progress = progress,
                color = Color(color),
                strokeWidth = 4.dp,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}