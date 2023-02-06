package com.omtorney.doer.goals.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.components.AppName
import com.omtorney.doer.core.presentation.components.BottomBar
import com.omtorney.doer.core.presentation.components.SettingsButton
import com.omtorney.doer.core.presentation.components.TopBar

@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalsViewModel = hiltViewModel(),
    onAddGoalClick: () -> Unit,
    onSettingsClick:() -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val scaffoldState = rememberScaffoldState()

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
        TopBar(modifier = Modifier.padding(8.dp)) {
            AppName(
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
            SettingsButton(
                accentColor = accentColor,
                onClick = onSettingsClick
            )
        }
    }
}