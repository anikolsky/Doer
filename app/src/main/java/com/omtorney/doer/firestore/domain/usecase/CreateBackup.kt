package com.omtorney.doer.firestore.domain.usecase

import com.google.gson.Gson
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.firestore.domain.CryptoManager
import com.omtorney.doer.notes.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import javax.inject.Inject

class CreateBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUserName: String, notes: List<Note>): Flow<FirestoreResult<String>> {
        val cryptoManager = CryptoManager()
        val notesJson = Gson().toJson(notes)
        val encryptedJson = cryptoManager.encrypt(notesJson.encodeToByteArray()).decodeToString()
        val firestoreUser = FirestoreUser(name = firestoreUserName, notes = encryptedJson)
        return repository.createBackup(firestoreUser)
    }
}