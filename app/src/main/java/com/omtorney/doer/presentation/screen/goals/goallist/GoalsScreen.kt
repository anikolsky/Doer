package com.omtorney.doer.presentation.screen.goals.goallist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.presentation.MainState
import com.omtorney.doer.presentation.Screen
import com.omtorney.doer.presentation.components.AppName
import com.omtorney.doer.presentation.components.BottomBar
import com.omtorney.doer.presentation.components.ScreenName
import com.omtorney.doer.presentation.components.SettingsButton
import com.omtorney.doer.presentation.components.TopBar
import com.omtorney.doer.presentation.screen.goals.components.GoalItem
import com.omtorney.doer.presentation.screen.goals.components.calculateProgress

@Composable
fun GoalsScreen(
    navController: NavController,
    mainState: MainState,
    goalState: GoalState,
    onEvent: (GoalEvent) -> Unit,
    onGoalClick: (Long) -> Unit,
    onAddGoalClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar {
                ScreenName(
                    title = Screen.Goals.label,
                    accentColor = mainState.accentColor,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    accentColor = mainState.accentColor,
                    onClick = onSettingsClick
                )
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                accentColor = mainState.accentColor
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddGoalClick() },
                containerColor = Color(mainState.accentColor),
                contentColor = contentColorFor(backgroundColor = Color(mainState.accentColor)),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(mainState.accentColor),
                            Color(mainState.secondaryColor)
                        ),
                        startX = 0f,
                        endX = LocalContext.current.resources.displayMetrics.widthPixels.dp.value
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(items = goalState.goals) { goal ->
                    GoalItem(
                        goal = goal,
                        progress = calculateProgress(goal.steps),
                        color = mainState.secondaryColor,
                        onGoalClick = { onGoalClick(goal.id!!) },
                        onLongClick = {}
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}
