package com.omtorney.doer.firestore.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsers @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<FirestoreResult<List<FirestoreUser?>>> {
        return repository.getUsers()
    }
}