package com.omtorney.doer.notes.domain.model

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
