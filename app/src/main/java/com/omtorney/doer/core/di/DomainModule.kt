package com.omtorney.doer.core.di

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.domain.usecase.*
import com.omtorney.doer.settings.domain.usecase.*
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
            setLineDivideState = SetLineDivideState(repository),
            getSecondaryColor = GetSecondaryColor(repository),
            setSecondaryColor = SetSecondaryColor(repository)
        )
    }
}