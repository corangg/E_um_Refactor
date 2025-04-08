package com.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LocalUserInfoData::class,
        LocalAddressItemData::class,
        LocalFriendData::class,
        LocalChatData::class,
        LocalScheduleData::class],
    version = 5,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun addressDao(): AddressDao
    abstract fun friendDao(): FriendDao
    abstract fun chatDao(): ChatDao
    abstract fun scheduleDao(): ScheduleDao
}