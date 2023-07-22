package com.omtorney.doer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omtorney.doer.data.local.database.AppDatabase
import com.omtorney.doer.data.local.database.GoalDao
import com.omtorney.doer.data.local.database.NoteDao
import com.omtorney.doer.data.local.datastore.SettingsStore
import com.omtorney.doer.util.Constants.DATA_STORE_NAME
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
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile(DATA_STORE_NAME)
        }
    }

    @Provides
    @Singleton
    fun provideSettingsStore(dataStore: DataStore<Preferences>): SettingsStore {
        return SettingsStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideFirestoreDatabaseReference(): CollectionReference {
        return Firebase.firestore.collection("users")
    }
}
