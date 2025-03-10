package com.data.datasource.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalUserInfoData(
    @PrimaryKey val id: Int = 1,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phone: String,
    val zoneCode: String,
    val address: String,
    val imgUrl: String = "",
    val statusMessage: String = "",
    val timeStamp: String = ""
)

@Entity
data class LocalAddressItemData(
    @PrimaryKey val address: String,
    val title: String,
    val zoneCode: String,
    val mainValue: Boolean
)