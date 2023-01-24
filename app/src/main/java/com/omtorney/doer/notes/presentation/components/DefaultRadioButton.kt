package com.omtorney.doer.notes.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.doer.core.presentation.theme.DoerTheme

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    color: Color,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = contentColorFor(backgroundColor = color),
                unselectedColor = color
            )
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = color
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultRadioButtonPreview() {
    DoerTheme {
        Row {
            DefaultRadioButton(
                text = "Selected",
                selected = true,
                color = Color.Blue,
                onSelect = {}
            )
            DefaultRadioButton(
                text = "Unselected",
                selected = false,
                color = Color.Blue,
                onSelect = {}
            )
        }
    }
}