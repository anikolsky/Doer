package com.omtorney.doer.presentation

import com.omtorney.doer.R

sealed class Screen(val route: String, val label: String, val icon: Int?) {
    object Notes : Screen("notes_screen", "Notes", R.drawable.ic_round_task)
    object NoteEdit : Screen("note_screen", "Edit note", null)
    object Goals : Screen("goals_screen", "Goals", R.drawable.ic_round_flag_circle)
    object GoalEdit : Screen("goal_screen", "Edit goal", null)
    object Settings : Screen("settings_screen", "Settings", null)
    object PermissionsRequest : Screen("permissions_screen", "Permissions request", null)
}
