package com.omtorney.doer.data.model.note

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
