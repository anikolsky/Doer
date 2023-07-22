package com.omtorney.doer.domain.usecases.firebase

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.remote.database.FirestoreResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsers @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>> {
        return repository.getUsers(userName)
    }
}
