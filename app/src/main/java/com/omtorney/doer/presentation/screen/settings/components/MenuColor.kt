package com.omtorney.doer.presentation.screen.settings.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R

@Composable
fun MenuColor(
    modifier: Modifier = Modifier,
    accentColor: Long, // ?
    backgroundColor: Long, // ?
    subtitle: String,
    onClick: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_round_color),
                contentDescription = null
            )
        },
        title = {
            Canvas(
                modifier = Modifier
                    .size(width = 40.dp, height = 15.dp)
                    .clip(RoundedCornerShape(6))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        RoundedCornerShape(6)
                    )
                    .background(color = Color(backgroundColor))
            ) {}
        },
        subtitle = subtitle
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(accentColor))
        ) {
            Text(text = "Select")
        }
    }
}
