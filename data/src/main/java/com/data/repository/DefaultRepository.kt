package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.data.mapper.toExternal
import com.data.mapper.toLocal
import com.domain.model.AddressItemData
import com.domain.model.AddressSaveResult
import com.domain.model.FriendItemData
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

    override fun getAddressListFlow() = localDataSource.getAddressDataListFlow().map { list -> list.map { it.toExternal() } }

    override suspend fun getAddressList() = withContext(ioDispatcher) {
        localDataSource.getAddressDataList().map { it.toExternal() }
    }

    override suspend fun upsertAddressList(addressData: AddressItemData) = withContext(ioDispatcher){
        localDataSource.upsertAddressData(addressData.toLocal())
    }

    override suspend fun deleteAddress(address: String) = withContext(ioDispatcher) {
        localDataSource.deleteAddressData(address)
    }

    override suspend fun deleteAllAddress() = withContext(ioDispatcher) {
        localDataSource.deleteAddressAllData()
    }

    override suspend fun checkAddressData(addressData: AddressItemData) = withContext(ioDispatcher) {
        val addressList = localDataSource.getAddressDataList()
        return@withContext if (addressList.any { it.address == addressData.address }) {
            AddressSaveResult.DuplicateAddress.code
        } else if (addressList.any { it.title == addressData.title }) {
            AddressSaveResult.DuplicateName.code
        } else {
            AddressSaveResult.Success.code
        }
    }

    override suspend fun updateAddressDataList(addressDataList: List<AddressItemData>) = withContext(ioDispatcher) {
        localDataSource.updateAddressDataList(addressDataList.map { it.toLocal() })
    }

    override fun getFriendListFlow() = localDataSource.getFriendDataListFlow().map { list -> list.map { it.toExternal() } }

    override suspend fun getFriendList() = withContext(ioDispatcher){
        localDataSource.getFriendDataList().map { it.toExternal()}
    }

    override suspend fun upsertFriendData(email: String, friendItemData: FriendItemData) = withContext(ioDispatcher) {
        localDataSource.upsertFriendData(friendItemData.toLocal(email))
    }

    override suspend fun deleteAllFriendData() = withContext(ioDispatcher) {
        localDataSource.deleteAllFriendData()
    }

    override suspend fun deleteFriendData(email: String) = withContext(ioDispatcher) {
        localDataSource.deleteFriendData(email)
    }
}