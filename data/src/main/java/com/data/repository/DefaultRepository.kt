package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.data.mapper.toExternal
import com.data.mapper.toLocal
import com.domain.model.UserInfo
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
    override suspend fun getUserInfo() = withContext(ioDispatcher) {
        localDataSource.getUserInfoData()?.toExternal()
    }

    override suspend fun upsertUserInfo(userInfo: UserInfo) = withContext(ioDispatcher) {
        localDataSource.upsertUserInfoData(userInfo.toLocal())
    }

    override fun getUserInfoFlow() = localDataSource.getUserInfoDataFlow().map { it.toExternal() }
}