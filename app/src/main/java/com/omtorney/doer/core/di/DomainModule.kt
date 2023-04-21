package com.omtorney.doer.core.di

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.domain.usecase.CreateBackup
import com.omtorney.doer.firestore.domain.usecase.DeleteBackup
import com.omtorney.doer.firestore.domain.usecase.FirestoreUseCases
import com.omtorney.doer.firestore.domain.usecase.GetUsers
import com.omtorney.doer.firestore.domain.usecase.UpdateBackup
import com.omtorney.doer.goals.domain.usecase.*
import com.omtorney.doer.notes.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: Repository): NoteUseCases {
        return NoteUseCases(
            addNote = AddNote(repository),
            deleteNote = DeleteNote(repository),
            getNote = GetNote(repository),
            getNotes = GetNotes(repository),
            pinNote = PinNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGoalUseCases(repository: Repository): GoalUseCases {
        return GoalUseCases(
            addGoal = AddGoal(repository),
            deleteGoal = DeleteGoal(repository),
            getGoal = GetGoal(repository),
            getGoals = GetGoals(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFirestoreUseCases(repository: Repository): FirestoreUseCases {
        return FirestoreUseCases(
            getUsers = GetUsers(repository),
            createBackup = CreateBackup(repository),
            updateBackup = UpdateBackup(repository),
            deleteBackup = DeleteBackup(repository)
        )
    }
}