package com.omtorney.doer.goals.presentation.goallist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.core.presentation.components.AppName
import com.omtorney.doer.core.presentation.components.BottomBar
import com.omtorney.doer.core.presentation.components.ScreenName
import com.omtorney.doer.core.presentation.components.SettingsButton
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.goals.presentation.components.GoalItem
import com.omtorney.doer.goals.presentation.components.calculateProgress

@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalsViewModel = hiltViewModel(),
    accentColor: Long,
    secondaryColor: Long,
    onGoalClick: (Long) -> Unit,
    onAddGoalClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopBar(
                color = accentColor,
                modifier = Modifier.padding(8.dp)
            ) {
                AppName(
                    accentColor = accentColor,
//                    modifier = Modifier.weight(1f)
                )
                ScreenName(
                    title = Screen.Goals.label,
                    accentColor = accentColor,
                    modifier = Modifier // TODO center
                )
                SettingsButton(
                    accentColor = accentColor,
                    onClick = onSettingsClick
                )
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                accentColor = accentColor
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddGoalClick() },
                backgroundColor = Color(accentColor),
                contentColor = contentColorFor(backgroundColor = Color(accentColor))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(accentColor),
                            Color(secondaryColor)
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
                items(items = state.goals) { goal ->
                    GoalItem(
                        goal = goal,
                        progress = calculateProgress(goal.steps),
                        color = secondaryColor,
                        onGoalClick = { onGoalClick(goal.id!!) },
                        onLongClick = {}
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}