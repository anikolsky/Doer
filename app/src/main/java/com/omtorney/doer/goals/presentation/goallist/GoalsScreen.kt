package com.omtorney.doer.goals.presentation.goallist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.core.presentation.components.*
import com.omtorney.doer.goals.presentation.components.GoalItem

@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalsViewModel = hiltViewModel(),
    onGoalClick: (Long) -> Unit,
    onAddGoalClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val secondaryColor by viewModel.secondaryColor.collectAsState()
    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value

    Scaffold(
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
//                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(accentColor),
                            Color(secondaryColor)
                        ),
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(
                            with(LocalDensity.current) { 600.dp.toPx() },
                            with(LocalDensity.current) { 600.dp.toPx() })
                    )
                )
        ) {
            TopBar(modifier = Modifier.padding(8.dp)) {
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = state.goals) { goal ->
                    GoalItem(
                        goal = goal,
                        onGoalClick = { onGoalClick(goal.id!!) },
                        onLongClick = {},
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}