package com.omtorney.doer.settings.presentation.signin

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)
