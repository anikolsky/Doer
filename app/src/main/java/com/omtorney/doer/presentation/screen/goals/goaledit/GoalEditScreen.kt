package com.omtorney.doer.presentation.screen.goals.goaledit

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omtorney.doer.R
import com.omtorney.doer.presentation.MainState
import com.omtorney.doer.presentation.Screen
import com.omtorney.doer.presentation.components.BackButton
import com.omtorney.doer.presentation.components.ScreenName
import com.omtorney.doer.presentation.components.TopBar
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

@Composable
fun GoalEditScreen(
    mainState: MainState,
    goalEditState: GoalEditState,
    onEvent: (GoalEditEvent) -> Unit,
    onClickClose: () -> Unit
) {
    var goalInfoExpanded by remember { mutableStateOf(false) }

//    val scaffoldState = rememberScaffoldState()
    val snackbarCoroutineScope = rememberCoroutineScope()

//    val focusRequester = remember { FocusRequester() }

    val animatedProgress = animateFloatAsState(
        targetValue = goalEditState.progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

//    LaunchedEffect(key1 = true) {
//        snackbarCoroutineScope.launch {
//            viewModel.eventFlow.collect { event ->
//                when (event) {
//                    is UiEvent.ShowSnackbar -> {
//                        scaffoldState.snackbarHostState.showSnackbar(message = event.message)
//                    }
//                    UiEvent.Save -> {
//                        scaffoldState.snackbarHostState.showSnackbar(message = "Saved")
//                    }
//                    UiEvent.HideSnackbar -> {
//                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
//                    }
//                }
//            }
//        }
//    }

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                BackButton(onClick = onClickClose)
                ScreenName(
                    title = Screen.GoalEdit.label,
                    accentColor = mainState.accentColor,
//                        modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        onEvent(GoalEditEvent.DeleteGoal(goalEditState.id!!))
//                        snackbarCoroutineScope.launch {
//                            scaffoldState.snackbarHostState.showSnackbar(
//                                message = "Deleted",
//                                duration = SnackbarDuration.Short
//                            )
//                        }
                        onClickClose()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_delete),
                        contentDescription = stringResource(R.string.delete),
                        tint = contentColorFor(backgroundColor = Color(mainState.accentColor))
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
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
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                /** GoalEdit description */
                Card {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        /** Progress */
                        LinearProgressIndicator(
                            progress = animatedProgress,
                            color = Color(mainState.secondaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                        BasicTextField(
                            value = goalEditState.title,
                            onValueChange = { onEvent(GoalEditEvent.EnterTitle(it)) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
//                            .focusRequester(focusRequester)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    /** Steps */
                    Text(
                        text = "Steps",
                        style = MaterialTheme.typography.titleMedium
                    )
                    /** Info button */
                    Box {
                        IconButton(onClick = { goalInfoExpanded = true }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "GoalEdit info"
                            )
                        }
                        NoteInfoMenu(
                            goalEditState = goalEditState,
                            expanded = goalInfoExpanded,
                            onDismissRequest = { goalInfoExpanded = false },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp)
                        .weight(1f)
                ) {
                    items(items = goalEditState.steps) { step ->
                        Card {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(IntrinsicSize.Min)
                            ) {
                                /** Achieve button */
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(
                                            color = if (step.isAchieved) Color(mainState.secondaryColor)
                                            else Color(mainState.accentColor)
                                        )
                                        .clickable {
                                            onEvent(GoalEditEvent.AchieveStep(step.id!!))
                                        },
                                ) {
                                    Text(
                                        text = "Step ${step.id!!.plus(1)}",
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                /** Step description */
                                BasicTextField(
                                    value = goalEditState.steps[step.id!!].text,
                                    onValueChange = {
                                        onEvent(
                                            GoalEditEvent.EditStep(
                                                id = step.id!!,
                                                text = it
                                            )
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (step.isAchieved) Color.Gray
                                        else MaterialTheme.colorScheme.onBackground,
                                        textDecoration = if (step.isAchieved) TextDecoration.LineThrough
                                        else TextDecoration.None
                                    ),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(1f)
                                )
                                /** Delete step button */
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(color = Color(mainState.accentColor))
                                        .clickable {
                                            onEvent(GoalEditEvent.DeleteStep(step.id!!))
                                        },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_round_remove_circle_outline),
                                        contentDescription = stringResource(R.string.delete),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                /** Add step button */
                Button(
                    onClick = { onEvent(GoalEditEvent.AddStep(id = goalEditState.steps.lastIndex)) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(mainState.accentColor),
                        contentColor = contentColorFor(backgroundColor = Color(mainState.accentColor))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_add_circle_outline),
                        contentDescription = "Add step"
                    )
                }
                /** Save goal button */
                Button(
                    onClick = {
                        onEvent(GoalEditEvent.SaveGoal)
                        onClickClose()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(mainState.accentColor),
                        contentColor = contentColorFor(backgroundColor = Color(mainState.accentColor))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_save_alt),
                        contentDescription = "Save goal"
                    )
                }
            }
        }
    }
}

@Composable
fun NoteInfoMenu(
    goalEditState: GoalEditState,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("dd MMM yyyy - HH:mm:ss", getDefault())
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column {
            Text(
                text = """
                    GoalEdit id: ${goalEditState.id}
                    Created at:
                    ${sdf.format(goalEditState.createdAt)}
                    Modified at:
                    ${sdf.format(goalEditState.modifiedAt)}
                """.trimIndent(),
                color = Color.Gray.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}
