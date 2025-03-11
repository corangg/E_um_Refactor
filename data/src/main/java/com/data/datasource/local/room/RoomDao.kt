package com.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfoData(entity: LocalUserInfoData)

    @Query("SELECT * FROM LocalUserInfoData")
    fun getUserInfoData(): LocalUserInfoData?

    @Query("SELECT * FROM LocalUserInfoData")
    fun getUserInfoDataFlow(): Flow<LocalUserInfoData>

    @Upsert
    suspend fun upsertUserInfoData(entity: LocalUserInfoData)

    @Query("DELETE FROM LocalUserInfoData")
    suspend fun deleteUserInfoData()
}

@Dao
interface AddressDao {
    @Query("SELECT * FROM LocalAddressItemData")
    fun getUserInfoData(): List<LocalAddressItemData>

    @Query("SELECT * FROM LocalAddressItemData")
    fun getUserInfoDataFlow(): Flow<List<LocalAddressItemData>>

    @Upsert
    suspend fun upsertAddressData(entity: LocalAddressItemData)

    @Update
    suspend fun updateAddressDataList(entityList: List<LocalAddressItemData>)

    @Query("DELETE FROM LocalAddressItemData WHERE address = :address")
    suspend fun deleteAddressData(address: String)

    @Query("DELETE FROM LocalAddressItemData")
    suspend fun deleteAddressAllData()
}

@Dao
interface FriendDao {
    @Query("SELECT * FROM LocalFriendData")
    fun getFriendDataList(): List<LocalFriendData>

    @Query("SELECT * FROM LocalFriendData")
    fun getFriendDataFlow(): Flow<List<LocalFriendData>>

    @Upsert
    suspend fun upsertFriendData(entity: LocalFriendData)

    @Update
    suspend fun updateFriendDataList(entityList: List<LocalFriendData>)

    @Query("DELETE FROM LocalFriendData WHERE email = :email")
    suspend fun deleteFriendData(email: String)

    @Query("DELETE FROM LocalFriendData")
    suspend fun deleteFriendAllData()
}