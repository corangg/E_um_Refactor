package com.data.di

import com.data.repository.DefaultFirebaseRepository
import com.data.repository.DefaultRepository
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
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
    abstract fun bindRepository(impl: DefaultRepository): Repository

    @Binds
    @Singleton
    abstract fun bindFirebaseRepository(impl: DefaultFirebaseRepository): FirebaseRepository
}