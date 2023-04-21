package com.omtorney.doer.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omtorney.doer.core.data.RepositoryImpl
import com.omtorney.doer.core.data.local.AppDatabase
import com.omtorney.doer.core.data.local.GoalDao
import com.omtorney.doer.core.data.local.NoteDao
import com.omtorney.doer.firestore.data.remote.FirestoreDatabase
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.util.Constants.DATA_STORE_NAME
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
    fun provideSettingsStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }

    @Provides
    @Singleton
    fun provideFirestoreDatabaseReference(): CollectionReference {
        return Firebase.firestore.collection("user")
    }

    @Provides
    @Singleton
    fun provideRepository(
        noteDao: NoteDao,
        goalDao: GoalDao,
        firestoreDatabase: FirestoreDatabase
    ): Repository {
        return RepositoryImpl(noteDao, goalDao, firestoreDatabase)
    }
}