package com.omtorney.doer.firestore.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> {
        return repository.updateBackup(firestoreUser)
    }
}