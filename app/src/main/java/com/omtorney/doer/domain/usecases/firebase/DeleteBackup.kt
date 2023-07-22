package com.omtorney.doer.domain.usecases.firebase

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.remote.database.FirestoreResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return repository.deleteBackup(firestoreUser)
    }
}
