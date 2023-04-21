package com.omtorney.doer.firestore.presentation

import com.omtorney.doer.firestore.data.FirestoreUser

data class FirestoreUserState(
    val data: List<FirestoreUser?>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
