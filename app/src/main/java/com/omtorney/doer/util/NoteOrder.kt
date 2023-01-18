package com.omtorney.doer.util

sealed class NoteOrder(val orderType: OrderType) {
    class Text(orderType: OrderType) : NoteOrder(orderType)
    class DateCreated(orderType: OrderType) : NoteOrder(orderType)
    class DateModified(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Text -> Text(orderType)
            is DateCreated -> DateCreated(orderType)
            is DateModified -> DateModified(orderType)
        }
    }
}