package com.omtorney.doer.data.remote

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.data.remote.database.FirestoreDatabase
import com.omtorney.doer.data.remote.database.FirestoreResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val firestoreDatabase: FirestoreDatabase
) : RemoteDataSource {

    override fun getUsers(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>> =
        firestoreDatabase.getUsers(userName)

    override suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.createBackup(firestoreUser)

    override suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.updateBackup(firestoreUser)

    override suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.deleteBackup(firestoreUser)
}
