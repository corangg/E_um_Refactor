package com.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalUserInfoData::class, LocalAddressItemData::class],
    version = 2,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun addressDao(): AddressDao
}