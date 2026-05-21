package com.muhdila.accurateuserapp.di

import com.muhdila.accurateuserapp.core.data.analytics.ConsoleAnalyticsTracker
import com.muhdila.accurateuserapp.core.domain.analytics.IAnalyticsTracker
import com.muhdila.accurateuserapp.user.data.repository.UserRepository
import com.muhdila.accurateuserapp.user.domain.repository.IUserRepository
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
    abstract fun bindUserRepository(impl: UserRepository): IUserRepository

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(impl: ConsoleAnalyticsTracker): IAnalyticsTracker
}
