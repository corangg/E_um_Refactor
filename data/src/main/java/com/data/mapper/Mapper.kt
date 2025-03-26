package com.data.mapper

import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalChatData
import com.data.datasource.local.room.LocalChatMessageData
import com.data.datasource.local.room.LocalFriendData
import com.data.datasource.local.room.LocalUserInfoData
import com.data.datasource.remote.RemoteAddition
import com.data.datasource.remote.RemoteAddressData
import com.data.datasource.remote.RemoteAddressElementData
import com.data.datasource.remote.RemoteArea0
import com.data.datasource.remote.RemoteArea1
import com.data.datasource.remote.RemoteCenter
import com.data.datasource.remote.RemoteCode
import com.data.datasource.remote.RemoteCoords
import com.data.datasource.remote.RemoteGeoCodeData
import com.data.datasource.remote.RemoteLand
import com.data.datasource.remote.RemoteMetaData
import com.data.datasource.remote.RemotePlaceItem
import com.data.datasource.remote.RemoteRegion
import com.data.datasource.remote.RemoteResult
import com.data.datasource.remote.RemoteReverseGeoCodeData
import com.data.datasource.remote.RemoteSearchData
import com.data.datasource.remote.RemoteStatus
import com.domain.model.Addition
import com.domain.model.AddressData
import com.domain.model.AddressElementData
import com.domain.model.AddressItemData
import com.domain.model.Area0
import com.domain.model.Area1
import com.domain.model.Center
import com.domain.model.ChatData
import com.domain.model.ChatMessageData
import com.domain.model.Code
import com.domain.model.Coords
import com.domain.model.FriendItemData
import com.domain.model.GeoCodeData
import com.domain.model.Land
import com.domain.model.MetaData
import com.domain.model.PlaceItem
import com.domain.model.Region
import com.domain.model.Result
import com.domain.model.ReverseGeoCodeData
import com.domain.model.SearchData
import com.domain.model.Status
import com.domain.model.UserInfo
import com.google.firebase.database.DataSnapshot

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

fun DataSnapshot.toExternal(): ChatMessageData = ChatMessageData(
    email = child("email").getValue(String::class.java) ?: "",
    nickname = child("nickname").getValue(String::class.java) ?: "",
    message = child("message").getValue(String::class.java) ?: "",
    time = child("time").getValue(String::class.java) ?: ""
)

fun ChatData.toLocal(): LocalChatData = LocalChatData(
    chatCode = this.chatCode,
    chatList = this.chatList.map { it.toLocalList() }
)

fun ChatMessageData.toLocalList(): LocalChatMessageData = LocalChatMessageData(
    email = this.email,
    nickname = this.nickname,
    message = this.message,
    time = this.time,
    isRead = this.isRead
)

fun LocalChatData.toExternal() = ChatData(
    chatCode = this.chatCode,
    chatList = this.chatList.map { it.toExternalList() }
)

fun LocalChatMessageData.toExternalList() = ChatMessageData(
    email = this.email,
    nickname = this.nickname,
    message = this.message,
    time = this.time,
    isRead = this.isRead
)

fun RemoteGeoCodeData.toExternal() = GeoCodeData(
    addresses = this.addresses.map { it.toExternal() },
    errorMessage = this.errorMessage,
    meta = this.meta.toExternal(),
    status = this.status
)

fun RemoteAddressData.toExternal() = AddressData(
    addressElements = this.addressElements.map { it.toExternal() },
    distance = this.distance,
    englishAddress = this.englishAddress,
    jibunAddress = this.jibunAddress,
    roadAddress = this.roadAddress,
    x = this.x,
    y = this.y
)

fun RemoteMetaData.toExternal() = MetaData(
    count = this.count,
    page = this.page,
    totalCount = this.totalCount
)

fun RemoteAddressElementData.toExternal() = AddressElementData(
    code = this.code,
    longName = this.longName,
    shortName = this.shortName,
    types = this.types
)

fun RemoteReverseGeoCodeData.toExternal() = ReverseGeoCodeData(
    results = this.results.map { it.toExternal() },
    status = this.status.toExternal()
)

fun RemoteResult.toExternal() = Result(
    code = this.code.toExternal(),
    land = this.land.toExternal(),
    name = this.name?:"",
    region = this.region.toExternal()
)

fun RemoteCode.toExternal() = Code(
    id = this.id?:"",
    mappingId = this.mappingId?:"",
    type = this.type?:""
)

fun RemoteLand.toExternal() = Land(
    addition0 = this.addition0.toExternal(),
    addition1 = this.addition1.toExternal(),
    addition2 = this.addition2.toExternal(),
    addition3 = this.addition3.toExternal(),
    addition4 = this.addition4.toExternal(),
    coords = this.coords.toExternal(),
    name = this.name?:"",
    number1 = this.number1?:"",
    number2 = this.number2?:"",
    type = this.type?:""
)

fun RemoteAddition.toExternal() = Addition(
    type = this.type?:"",
    value = this.value?:""
)

fun RemoteCoords.toExternal() = Coords(
    center = this.center.toExternal()
)

fun RemoteCenter.toExternal() = Center(
    crs = this.crs?:"",
    x = this.x,
    y = this.y
)

fun RemoteRegion.toExternal() = Region(
    area0 = this.area0.toExternal(),
    area1 = this.area1.toExternal(),
    area2 = this.area2.toExternal(),
    area3 = this.area3.toExternal(),
    area4 = this.area4.toExternal()
)

fun RemoteArea0.toExternal() = Area0(
    coords = this.coords.toExternal(),
    name = this.name?:""
)

fun RemoteArea1.toExternal() = Area1(
    alias = this.alias?:"",
    coords = this.coords.toExternal(),
    name = this.name?:""
)

fun RemoteStatus.toExternal() = Status(
    code = this.code,
    message = this.message?:"",
    name = this.name?:""
)

fun RemoteSearchData.toExternal() = SearchData(
    items = this.items.map { it.toExternal() }
)

fun RemotePlaceItem.toExternal() = PlaceItem(
    title = this.title,
    link = this.link,
    category = this.category,
    description = this.description,
    address = this.address,
    roadAddress = this.roadAddress,
    x = this.x,
    y = this.y
)