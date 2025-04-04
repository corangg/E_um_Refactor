package com.data.di

import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteNaverMapDataSource
import com.data.datasource.RemoteNaverSearchDataSource
import com.data.datasource.RemoteTMapDataSource
import com.data.datasource.local.DefaultLocalDataSource
import com.data.datasource.remote.NaverMapRemoteDataSource
import com.data.datasource.remote.NaverSearchRemoteDataSource
import com.data.datasource.remote.TMapRemoteDataSource
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

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteNaverMapDataSource(impl: NaverMapRemoteDataSource): RemoteNaverMapDataSource

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteNaverSearchDataSource(impl: NaverSearchRemoteDataSource): RemoteNaverSearchDataSource

    @Binds
    @Singleton
    @RemoteDataSources
    abstract fun bindDefaultRemoteTMapDataSource(impl: TMapRemoteDataSource): RemoteTMapDataSource
}