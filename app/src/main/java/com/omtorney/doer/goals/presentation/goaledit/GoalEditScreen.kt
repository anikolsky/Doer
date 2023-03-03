package com.omtorney.doer.goals.presentation.goaledit

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.doer.R
import com.omtorney.doer.core.presentation.Screen
import com.omtorney.doer.core.presentation.components.BackButton
import com.omtorney.doer.core.presentation.components.ScreenName
import com.omtorney.doer.core.presentation.components.TopBar
import com.omtorney.doer.core.presentation.components.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

@Composable
fun GoalEditScreen(
    viewModel: GoalEditViewModel = hiltViewModel(),
    onClickClose: () -> Unit
) {
    val accentColor by viewModel.accentColor.collectAsState()
    val secondaryColor by viewModel.secondaryColor.collectAsState()
    val state = viewModel.state.value
    var goalInfoExpanded by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

//    val focusRequester = remember { FocusRequester() }

    val animatedProgress = animateFloatAsState(
        targetValue = state.progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.Save -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = "Saved")
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
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
                            with(LocalDensity.current) { 600.dp.toPx() }
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                TopBar(modifier = Modifier.padding(vertical = 8.dp)) {
                    BackButton(onClick = onClickClose)
                    ScreenName(
                        title = Screen.Goal.label,
                        accentColor = accentColor,
//                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            viewModel.onEvent(GoalEditEvent.DeleteGoal(state.id!!))
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Deleted",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            onClickClose()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_delete),
                            contentDescription = stringResource(R.string.delete),
                            tint = contentColorFor(backgroundColor = Color(accentColor))
                        )
                    }
                }
                /** Goal description */
                Card {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        /** Progress */
                        LinearProgressIndicator(
                            progress = animatedProgress,
                            color = Color(secondaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        )
                        BasicTextField(
                            value = state.title,
                            onValueChange = { viewModel.onEvent(GoalEditEvent.EnterTitle(it)) },
                            textStyle = MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
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
                        style = MaterialTheme.typography.h6
                    )
                    /** Info button */
                    Box {
                        IconButton(onClick = { goalInfoExpanded = true }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Goal info"
                            )
                        }
                        NoteInfoMenu(
                            state = state,
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
                    items(items = state.steps) { step ->
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
                                            color = if (step.isAchieved) Color(secondaryColor)
                                            else Color(accentColor)
                                        )
                                        .clickable {
                                            viewModel.onEvent(GoalEditEvent.AchieveStep(step.id!!))
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
                                    value = state.steps[step.id!!].text,
                                    onValueChange = {
                                        viewModel.onEvent(
                                            GoalEditEvent.EditStep(
                                                id = step.id!!,
                                                text = it
                                            )
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.body1.copy(
                                        color = if (step.isAchieved) Color.Gray
                                        else MaterialTheme.colors.onBackground,
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
                                        .background(color = Color(accentColor))
                                        .clickable {
                                            viewModel.onEvent(GoalEditEvent.DeleteStep(step.id!!))
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
                    onClick = { viewModel.onEvent(GoalEditEvent.AddStep(id = state.steps.lastIndex)) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(accentColor),
                        contentColor = contentColorFor(backgroundColor = Color(accentColor))
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
                        viewModel.onEvent(GoalEditEvent.SaveGoal)
                        onClickClose()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(accentColor),
                        contentColor = contentColorFor(backgroundColor = Color(accentColor))
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
    state: GoalEditState,
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
                    Goal id: ${state.id}
                    Created at:
                    ${sdf.format(state.createdAt)}
                    Modified at:
                    ${sdf.format(state.modifiedAt)}
                """.trimIndent(),
                color = Color.Gray.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}