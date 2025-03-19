package com.data.datasource.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.data.datasource.local.room.converter.ChatDataConverter

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

@Entity
data class LocalFriendData(
    @PrimaryKey val email: String,
    val nickname: String,
    val statusMessage: String,
    val imgUrl: String
)

@Entity
@TypeConverters(ChatDataConverter::class)
data class LocalChatData(
    @PrimaryKey val chatCode: String,
    val chatList: List<LocalChatMessageData>
)

data class LocalChatMessageData(
    val email: String,
    val nickname: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)