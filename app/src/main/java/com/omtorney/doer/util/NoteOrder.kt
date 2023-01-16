package com.omtorney.doer.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class DateCreated(orderType: OrderType): NoteOrder(orderType)
    class DateModified(orderType: OrderType): NoteOrder(orderType)
}