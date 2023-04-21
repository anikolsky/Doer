package com.omtorney.doer.firestore.domain.usecase

import com.google.gson.Gson
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateBackup @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(firestoreUserName: String, notes: List<Note>): Flow<FirestoreResult<String>> {
        val notesJson = Gson().toJson(notes)
        val firestoreUser = FirestoreUser(name = firestoreUserName, notes = notesJson)
        return repository.createBackup(firestoreUser)
    }
}