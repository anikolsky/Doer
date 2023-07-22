package com.omtorney.doer.presentation.screen.settings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SignActions(
    modifier: Modifier = Modifier,
    color: Long,
    title: @Composable () -> Unit,
    subtitle: String,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = null,
        title = title,
        subtitle = subtitle
    ) {
        Button(
            onClick = onSignInClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Sign in")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSignOutClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Sign out")
        }
    }
}
