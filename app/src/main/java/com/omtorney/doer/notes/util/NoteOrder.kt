package com.omtorney.doer.notes.util

sealed class NoteOrder(val orderType: OrderType) {
    class Priority(orderType: OrderType) : NoteOrder(orderType)
    class Text(orderType: OrderType) : NoteOrder(orderType)
    class DateCreated(orderType: OrderType) : NoteOrder(orderType)
    class DateModified(orderType: OrderType) : NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Priority -> Priority(orderType)
            is Text -> Text(orderType)
            is DateCreated -> DateCreated(orderType)
            is DateModified -> DateModified(orderType)
        }
    }
}