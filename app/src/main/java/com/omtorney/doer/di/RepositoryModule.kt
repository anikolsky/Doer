package com.omtorney.doer.di

import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.RepositoryImpl
import com.omtorney.doer.data.local.LocalDataSource
import com.omtorney.doer.data.local.LocalDataSourceImpl
import com.omtorney.doer.data.remote.RemoteDataSource
import com.omtorney.doer.data.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}
