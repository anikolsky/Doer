package com.omtorney.doer.firestore.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDatabase @Inject constructor(
    private val reference: CollectionReference
) {

    fun getUsers(): Flow<FirestoreResult<List<FirestoreUser>>> = callbackFlow {
        trySend(FirestoreResult.Loading)
        val listener = reference.addSnapshotListener { value, error ->
            if (error != null) trySend(FirestoreResult.Error(Throwable(error.message)))
            if (value != null) {
                val users = value.map { documentSnapshot ->
                    documentSnapshot.toObject<FirestoreUser>()
                }
                trySend(FirestoreResult.Success(users))
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            val documentID = reference.document().id
            reference.document(documentID).set(firestoreUser.copy(id = documentID))
                .addOnSuccessListener { trySend(FirestoreResult.Success("Backup created")) }
                .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            awaitClose { close() }
        }

    suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            if (firestoreUser.id != null) {
                reference.document(firestoreUser.id).update(firestoreUser.toMap())
                    .addOnSuccessListener { trySend(FirestoreResult.Success("Backup updated")) }
                    .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            }
            awaitClose { close() }
        }

    suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        callbackFlow {
            if (firestoreUser.id != null) {
                reference.document(firestoreUser.id).delete()
                    .addOnSuccessListener { trySend(FirestoreResult.Success("Backup deleted")) }
                    .addOnFailureListener { trySend(FirestoreResult.Error(Throwable(it.message))) }
            }
            awaitClose { close() }
        }
}
