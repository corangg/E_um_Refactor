package com.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.data.datasource.local.room.AddressDao
import com.data.datasource.local.room.ChatDao
import com.data.datasource.local.room.Database
import com.data.datasource.local.room.FriendDao
import com.data.datasource.local.room.ScheduleDao
import com.data.datasource.local.room.UserInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalAddressItemData (address TEXT PRIMARY KEY NOT NULL, title TEXT NOT NULL, zoneCode TEXT NOT NULL, mainValue INTEGER NOT NULL DEFAULT 0)"
            )
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalFriendData (email TEXT PRIMARY KEY NOT NULL, nickname TEXT NOT NULL, statusMessage TEXT NOT NULL, imgUrl TEXT NOT NULL)"
            )
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalChatData (chatCode TEXT PRIMARY KEY NOT NULL, chatList TEXT NOT NULL)"
            )
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE LocalScheduleData (time TEXT PRIMARY KEY NOT NULL, email TEXT NOT NULL, nickname TEXT NOT NULL, startAddress TEXT NOT NULL, scheduleAddress TEXT NOT NULL, alarmTime TEXT NOT NULL, transportType TEXT NOT NULL, requestValue INTEGER NOT NULL DEFAULT 0)"
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "Database.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .build()

    @Provides
    fun provideUserInfoDao(database: Database): UserInfoDao = database.userInfoDao()

    @Provides
    fun provideAddressDao(database: Database): AddressDao = database.addressDao()

    @Provides
    fun provideFriendDao(database: Database): FriendDao = database.friendDao()

    @Provides
    fun provideChatDao(database: Database): ChatDao = database.chatDao()

    @Provides
    fun provideScheduleDao(database: Database): ScheduleDao = database.scheduleDao()
}