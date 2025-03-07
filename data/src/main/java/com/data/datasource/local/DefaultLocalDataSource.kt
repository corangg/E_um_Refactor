package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.LocalUserInfoData
import com.data.datasource.local.room.UserInfoDao
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val userInfoDao: UserInfoDao
) : LocalDataSource {
    override suspend fun insertUserInfoData(entity: LocalUserInfoData) =
        userInfoDao.insertUserInfoData(entity)

    override suspend fun getUserInfoData() = userInfoDao.getUserInfoData()

    override fun getUserInfoDataFlow() = userInfoDao.getUserInfoDataFlow()

    override suspend fun upsertUserInfoData(entity: LocalUserInfoData) = userInfoDao.upsertUserInfoData(entity)

    override suspend fun deleteUserInfoData() = userInfoDao.deleteUserInfoData()
}