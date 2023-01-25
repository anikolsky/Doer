package com.omtorney.doer.notes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.omtorney.doer.core.presentation.theme.DoerTheme
import com.omtorney.doer.notes.util.NoteOrder
import com.omtorney.doer.notes.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.background(color = MaterialTheme.colors.surface.copy(alpha = 0.9f))
    ) {
        Column {
            DefaultRadioButton(
                text = "Priority",
                selected = noteOrder is NoteOrder.Priority,
                color = color,
                onSelect = { onOrderChange(NoteOrder.Priority(noteOrder.orderType)) }
            )
            DefaultRadioButton(
                text = "Text",
                selected = noteOrder is NoteOrder.Text,
                color = color,
                onSelect = { onOrderChange(NoteOrder.Text(noteOrder.orderType)) }
            )
            DefaultRadioButton(
                text = "Created",
                selected = noteOrder is NoteOrder.DateCreated,
                color = color,
                onSelect = { onOrderChange(NoteOrder.DateCreated(noteOrder.orderType)) }
            )
            DefaultRadioButton(
                text = "Modified",
                selected = noteOrder is NoteOrder.DateModified,
                color = color,
                onSelect = { onOrderChange(NoteOrder.DateModified(noteOrder.orderType)) }
            )
        }
        Column {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                color = color,
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) }
            )
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                color = color,
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderSectionPreview() {
    DoerTheme {
        OrderSection(onOrderChange = {}, color = Color.Blue)
    }
}