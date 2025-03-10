package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.AddressDao
import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalUserInfoData
import com.data.datasource.local.room.UserInfoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val userInfoDao: UserInfoDao,
    private val addressDao: AddressDao
) : LocalDataSource {
    override suspend fun insertUserInfoData(entity: LocalUserInfoData) = userInfoDao.insertUserInfoData(entity)
    override suspend fun getUserInfoData() = userInfoDao.getUserInfoData()
    override fun getUserInfoDataFlow() = userInfoDao.getUserInfoDataFlow()
    override suspend fun upsertUserInfoData(entity: LocalUserInfoData) = userInfoDao.upsertUserInfoData(entity)
    override suspend fun deleteUserInfoData() = userInfoDao.deleteUserInfoData()

    override suspend fun upsertAddressData(entity: LocalAddressItemData) = addressDao.upsertAddressData(entity)
    override suspend fun updateAddressDataList(entityList: List<LocalAddressItemData>) = addressDao.updateAddressDataList(entityList)
    override suspend fun getAddressDataList() = addressDao.getUserInfoData()
    override fun getAddressDataListFlow() = addressDao.getUserInfoDataFlow()
    override suspend fun deleteAddressData(address: String) = addressDao.deleteAddressData(address)
    override suspend fun deleteAddressAllData() = addressDao.deleteAddressAllData()
}
