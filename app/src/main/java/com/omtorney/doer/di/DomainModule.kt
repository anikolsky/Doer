package com.omtorney.doer.di

import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.domain.usecases.firebase.CreateBackup
import com.omtorney.doer.domain.usecases.firebase.DeleteBackup
import com.omtorney.doer.domain.usecases.FirestoreUseCases
import com.omtorney.doer.domain.usecases.firebase.GetUsers
import com.omtorney.doer.domain.usecases.firebase.UpdateBackup
import com.omtorney.doer.domain.usecases.goals.AddGoal
import com.omtorney.doer.domain.usecases.goals.DeleteGoal
import com.omtorney.doer.domain.usecases.goals.GetGoal
import com.omtorney.doer.domain.usecases.goals.GetGoals
import com.omtorney.doer.domain.usecases.GoalUseCases
import com.omtorney.doer.domain.usecases.NoteUseCases
import com.omtorney.doer.domain.usecases.SettingsUseCases
import com.omtorney.doer.domain.usecases.notes.AddNote
import com.omtorney.doer.domain.usecases.notes.DeleteNote
import com.omtorney.doer.domain.usecases.notes.GetNote
import com.omtorney.doer.domain.usecases.notes.GetNotes
import com.omtorney.doer.domain.usecases.notes.PinNote
import com.omtorney.doer.domain.usecases.settings.GetAccentColor
import com.omtorney.doer.domain.usecases.settings.GetNoteSeparatorSize
import com.omtorney.doer.domain.usecases.settings.GetNoteSeparatorState
import com.omtorney.doer.domain.usecases.settings.GetSecondaryColor
import com.omtorney.doer.domain.usecases.settings.SetAccentColor
import com.omtorney.doer.domain.usecases.settings.SetNoteSeparatorSize
import com.omtorney.doer.domain.usecases.settings.SetNoteSeparatorState
import com.omtorney.doer.domain.usecases.settings.SetSecondaryColor
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
    fun provideSettingsUseCases(repository: Repository): SettingsUseCases {
        return SettingsUseCases(
            getAccentColor = GetAccentColor(repository),
            setAccentColor = SetAccentColor(repository),
            getSecondaryColor = GetSecondaryColor(repository),
            setSecondaryColor = SetSecondaryColor(repository),
            getNoteSeparatorState = GetNoteSeparatorState(repository),
            setNoteSeparatorState = SetNoteSeparatorState(repository),
            getNoteSeparatorSize = GetNoteSeparatorSize(repository),
            setNoteSeparatorSize = SetNoteSeparatorSize(repository)
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
