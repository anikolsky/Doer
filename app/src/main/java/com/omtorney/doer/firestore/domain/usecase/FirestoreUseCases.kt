package com.omtorney.doer.firestore.domain.usecase

data class FirestoreUseCases(
    val getUsers: GetUsers,
    val createBackup: CreateBackup,
    val deleteBackup: DeleteBackup,
    val updateBackup: UpdateBackup
)