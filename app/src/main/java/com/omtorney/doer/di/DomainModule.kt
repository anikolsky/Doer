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
            getNote = GetNote(repository),
            addNote = AddNote(repository),
            deleteNote = DeleteNote(repository),
            pinNote = PinNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(repository: Repository): SettingsUseCases {
        return SettingsUseCases(
            getAccentColor = GetAccentColor(repository),
            setAccentColor = SetAccentColor(repository),
            getLineDivideState = GetLineDivideState(repository),
            setLineDivideState = SetLineDivideState(repository)
        )
    }
}