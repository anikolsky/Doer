package com.omtorney.doer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.doer.ui.theme.DoerTheme
import com.omtorney.doer.util.NoteOrder
import com.omtorney.doer.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row {
            DefaultRadioButton(
                text = "Text",
                selected = noteOrder is NoteOrder.Text,
                onSelect = { onOrderChange(NoteOrder.Text(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Created",
                selected = noteOrder is NoteOrder.DateCreated,
                onSelect = { onOrderChange(NoteOrder.DateCreated(noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Modified",
                selected = noteOrder is NoteOrder.DateModified,
                onSelect = { onOrderChange(NoteOrder.DateModified(noteOrder.orderType)) }
            )
        }
//        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = modifier) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderSectionPreview() {
    DoerTheme {
        OrderSection(onOrderChange = {})
    }
}