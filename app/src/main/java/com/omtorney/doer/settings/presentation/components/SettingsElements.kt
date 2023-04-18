package com.omtorney.doer.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MenuSwitcher(
    modifier: Modifier = Modifier,
    color: Long,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(Color(color))
        )
    }
}

@Composable
fun MenuSlider(
    modifier: Modifier = Modifier,
    color: Long,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onSlide: (Float) -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Slider(
            modifier = Modifier.width(100.dp),
            value = value,
            valueRange = valueRange,
            steps = 1,
            onValueChange = { onSlide(it) },
            colors = SliderDefaults.colors(
                thumbColor = Color(color),
                activeTrackColor = Color(color)
            )
        )
    }
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    color: Long,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    buttonText: String,
    onClick: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = buttonText)
        }
    }
}

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

@Composable
fun DatabaseActions(
    modifier: Modifier = Modifier,
    color: Long,
    title: @Composable () -> Unit,
    subtitle: String,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = null,
        title = title,
        subtitle = subtitle
    ) {
        Button(
            onClick = onExportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Export")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onImportClick,
            colors = ButtonDefaults.buttonColors(Color(color))
        ) {
            Text(text = "Import")
        }
    }
}

@Composable
fun ElementFrame(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        icon?.invoke()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            title()
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
        content()
    }
}