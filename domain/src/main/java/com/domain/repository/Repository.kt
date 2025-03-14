package com.domain.repository

import com.domain.model.AddressItemData
import com.domain.model.FriendItemData
import com.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getUserInfo(): UserInfo?

    suspend fun upsertUserInfo(userInfo: UserInfo)

    fun getUserInfoFlow(): Flow<UserInfo>

    fun getAddressListFlow(): Flow<List<AddressItemData>>

    suspend fun getAddressList(): List<AddressItemData>

    suspend fun upsertAddressList(addressData: AddressItemData)

    suspend fun deleteAddress(address: String)

    suspend fun deleteAllAddress()

    suspend fun checkAddressData(addressData: AddressItemData): Int

    suspend fun updateAddressDataList(addressDataList: List<AddressItemData>)

    fun getFriendListFlow(): Flow<List<FriendItemData>>

    suspend fun getFriendList(): List<FriendItemData>

    suspend fun upsertFriendData(email: String, friendItemData: FriendItemData)

    suspend fun deleteAllFriendData()

    suspend fun deleteFriendData(email: String)
}