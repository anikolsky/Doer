package com.omtorney.doer.firebase.auth

import com.omtorney.doer.data.remote.database.FirestoreUser

data class SignInResult(
    val firestoreUser: FirestoreUser?,
    val errorMessage: String?
)
