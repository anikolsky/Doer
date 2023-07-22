package com.omtorney.doer.domain.usecases

import com.omtorney.doer.domain.usecases.firebase.CreateBackup
import com.omtorney.doer.domain.usecases.firebase.DeleteBackup
import com.omtorney.doer.domain.usecases.firebase.GetUsers
import com.omtorney.doer.domain.usecases.firebase.UpdateBackup

data class FirestoreUseCases(
    val getUsers: GetUsers,
    val createBackup: CreateBackup,
    val deleteBackup: DeleteBackup,
    val updateBackup: UpdateBackup
)
