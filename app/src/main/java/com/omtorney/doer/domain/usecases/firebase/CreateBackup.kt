package com.omtorney.doer.domain.usecases.firebase

import com.google.gson.Gson
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.remote.database.FirestoreResult
import com.omtorney.doer.firebase.CryptoManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUser: FirestoreUser, notes: List<Note>): Flow<FirestoreResult<String>> {
        val cryptoManager = CryptoManager()
        val notesJson = Gson().toJson(notes)
        val encryptedJson = cryptoManager.encrypt(notesJson.encodeToByteArray()).decodeToString()
        val firestoreUser = FirestoreUser(name = firestoreUser.name, notes = notesJson)
        return repository.createBackup(firestoreUser)
    }
}
