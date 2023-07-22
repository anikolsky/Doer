package com.omtorney.doer.data.remote

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.data.remote.database.FirestoreResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun getUsers(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>>
    suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
}
