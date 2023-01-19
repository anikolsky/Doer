package com.omtorney.doer.notes.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
