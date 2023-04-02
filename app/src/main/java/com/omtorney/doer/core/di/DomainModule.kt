package com.omtorney.doer.core.di

import com.omtorney.doer.core.domain.Repository
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
}