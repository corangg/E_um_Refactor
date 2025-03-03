package com.data.di

import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.local.DefaultLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    @LocalDataSources
    abstract fun bindDefaultLocalDataSource(impl: DefaultLocalDataSource): LocalDataSource
}