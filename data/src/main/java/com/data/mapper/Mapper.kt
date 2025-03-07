package com.data.mapper

import com.data.datasource.local.room.LocalUserInfoData
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