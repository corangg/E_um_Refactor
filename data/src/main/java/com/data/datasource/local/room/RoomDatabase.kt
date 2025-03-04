package com.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalAccountData::class],
    version = 1,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun accountDataDao(): AccountDao
}