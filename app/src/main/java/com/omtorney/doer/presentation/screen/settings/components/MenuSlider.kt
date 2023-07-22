package com.omtorney.doer.presentation.screen.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        Row(verticalAlignment = Alignment.CenterVertically) {
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
            Text(
                text = "${value.toInt()}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
