package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.AccountDao
import com.data.datasource.local.room.LocalAccountData
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val accountDao: AccountDao
) : LocalDataSource {
    override suspend fun insertAccountData(entity: LocalAccountData) =
        accountDao.insertAccountData(entity)

    override fun getAccountData() = accountDao.getAccountDataFlow()

    override suspend fun deleteAccountData() = accountDao.deleteAccountData()
}