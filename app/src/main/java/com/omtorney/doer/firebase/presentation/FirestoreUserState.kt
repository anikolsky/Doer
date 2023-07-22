package com.omtorney.doer.firebase.presentation

import com.omtorney.doer.data.remote.database.FirestoreUser

data class FirestoreUserState(
    val data: List<FirestoreUser?>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
