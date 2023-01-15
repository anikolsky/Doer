package com.omtorney.doer.di

import com.omtorney.doer.domain.AccentColorUseCase
import com.omtorney.doer.domain.LineSeparatorStateUseCase
import com.omtorney.doer.domain.Repository
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
    fun provideAccentColorUseCase(repository: Repository): AccentColorUseCase {
        return AccentColorUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLineSeparatorStateUseCase(repository: Repository): LineSeparatorStateUseCase {
        return LineSeparatorStateUseCase(repository)
    }
}