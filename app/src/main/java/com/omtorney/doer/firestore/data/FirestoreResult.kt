package com.omtorney.doer.firestore.data

sealed class FirestoreResult<out R> {
    data class Success<out T>(val data: T) : FirestoreResult<T>()
    data class Error(val exception: Throwable) : FirestoreResult<Nothing>()
    object Loading : FirestoreResult<Nothing>()
}
