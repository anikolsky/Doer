package com.omtorney.doer.core.di

import android.content.Context
import androidx.room.Room
import com.omtorney.doer.core.data.RepositoryImpl
import com.omtorney.doer.settings.data.SettingsStore
import com.omtorney.doer.core.data.database.AppDatabase
import com.omtorney.doer.core.data.database.GoalDao
import com.omtorney.doer.core.data.database.NoteDao
import com.omtorney.doer.core.domain.Repository
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
            "doer_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

    @Provides
    @Singleton
    fun provideGoalDao(appDatabase: AppDatabase): GoalDao {
        return appDatabase.goalDao()
    }

    @Provides
    @Singleton
    fun provideSettingsStore(@ApplicationContext appContext: Context): SettingsStore {
        return SettingsStore(appContext)
    }

    @Provides
    @Singleton
    fun provideRepository(
        noteDao: NoteDao,
        goalDao: GoalDao,
        settingsStore: SettingsStore
    ): Repository {
        return RepositoryImpl(noteDao, goalDao, settingsStore)
    }
}