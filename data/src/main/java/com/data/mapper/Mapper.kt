package com.data.mapper

import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalFriendData
import com.data.datasource.local.room.LocalUserInfoData
import com.domain.model.AddressItemData
import com.domain.model.FriendItemData
import com.domain.model.UserInfo

fun UserInfo.toLocal() = LocalUserInfoData(
    email = this.email,
    password = this.password,
    name = this.name,
    nickname = this.nickname,
    phone = this.phone,
    zoneCode = this.zoneCode,
    address = this.address,
    imgUrl = this.imgUrl,
    statusMessage = this.statusMessage,
    timeStamp = this.timeStamp
)

fun LocalUserInfoData.toExternal() = UserInfo(
    email = this.email,
    password = this.password,
    name = this.name,
    nickname = this.nickname,
    phone = this.phone,
    zoneCode = this.zoneCode,
    address = this.address,
    imgUrl = this.imgUrl,
    statusMessage = this.statusMessage,
    timeStamp = this.timeStamp
)

fun LocalAddressItemData.toExternal() = AddressItemData(
    address = this.address,
    title = this.title,
    zoneCode = this.zoneCode,
    mainValue = this.mainValue
)

fun AddressItemData.toLocal() = LocalAddressItemData(
    address = this.address,
    title = this.title,
    zoneCode = this.zoneCode,
    mainValue = this.mainValue
)

fun LocalFriendData.toExternal() = FriendItemData(
    nickName = this.nickname,
    statusMessage = this.statusMessage,
    profileUrl = this.imgUrl,
    email = this.email
)

fun FriendItemData.toLocal(email: String) = LocalFriendData(
    email = email,
    nickname = this.nickName,
    statusMessage = this.statusMessage,
    imgUrl = this.profileUrl
)