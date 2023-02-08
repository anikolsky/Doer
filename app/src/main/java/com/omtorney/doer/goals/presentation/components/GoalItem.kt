package com.omtorney.doer.goals.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NotePriorityConverter
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalItem(
    goal: Goal,
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
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = goal.title,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}