package com.omtorney.doer.di

import com.omtorney.doer.domain.*
import com.omtorney.doer.domain.usecase.*
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
            getNotes = GetNotes(repository),
            addNote = AddNote(repository),
            deleteNote = DeleteNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAccentColorUseCase(repository: Repository): AccentColor {
        return AccentColor(repository)
    }

    @Provides
    @Singleton
    fun provideLineSeparatorStateUseCase(repository: Repository): LineSeparatorState {
        return LineSeparatorState(repository)
    }
}