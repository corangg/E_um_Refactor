package com.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.data.datasource.local.room.AddressDao
import com.data.datasource.local.room.Database
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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "Database.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideUserInfoDao(database: Database): UserInfoDao = database.userInfoDao()

    @Provides
    fun provideAddressDao(database: Database): AddressDao = database.addressDao()
}