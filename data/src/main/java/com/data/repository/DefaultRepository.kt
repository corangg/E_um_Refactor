package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
}