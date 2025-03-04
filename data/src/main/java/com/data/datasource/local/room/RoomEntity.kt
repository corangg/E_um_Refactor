package com.data.datasource.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalAccountData(
    @PrimaryKey val id: Int = 1,
    val accountId: String,
    val accountPassword: String
)