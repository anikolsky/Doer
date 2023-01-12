package com.omtorney.doer.di

import android.content.Context
import androidx.room.Room
import com.omtorney.doer.data.Repository
import com.omtorney.doer.data.database.AppDatabase
import com.omtorney.doer.data.database.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "doer_notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(appDatabase: AppDatabase) : NoteDao {
        return appDatabase.noteDao()
    }

    @Provides
    @Singleton
    fun provideRepository(noteDao: NoteDao): Repository {
        return Repository(noteDao)
    }
}