package com.domain.model

data class UserInfo(
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

data class AddressItemData(
    val title: String = "집",
    val address: String,
    val zoneCode: String,
    val mainValue: Boolean = false
)

data class FriendItemData(
    val nickName: String,
    val statusMessage: String,
    val profileUrl: String,
    val email: String
)

data class ChatMessageData(
    val email: String,
    val nickname: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val imgUrl: String = "",
    val myMessage: Boolean = true,
    val messageType: Int = 1
)

data class ChatData(
    val chatCode: String,
    val chatList: List<ChatMessageData>
)

data class ChatRoomItemData(
    val chatCode: String,
    val name: String,
    val lastMessage: String,
    val notReadCount: Int = 0,
    val imgUrl: String
)

sealed class AlarmData(open val time: String) {
    data class RequestFriendAlarmData(
        val email: String,
        val nickName: String,
        override val time: String
    ) : AlarmData(time)

    data class ResponseFriendAlarmData(
        val email: String,
        val nickName: String,
        val value: Boolean,
        override val time: String
    ) : AlarmData(time)
}

sealed class SignUpResult {
    data object Success : SignUpResult()
    data object AlreadyExists : SignUpResult()
    data object Failure : SignUpResult()
}

sealed class SignInResult(val code: Int) {
    data object Success : SignInResult(1)
    data object InvalidEmail : SignInResult(2)
    data object UserNotFound : SignInResult(3)
    data object InvalidPassword : SignInResult(4)
    data object Failure : SignInResult(5)
}

sealed class AddressSaveResult(val code: Int) {
    data object DuplicateAddress : SignInResult(1)
    data object DuplicateName : SignInResult(2)
    data object Success : SignInResult(3)
}

sealed class FriendRequestResult(val code: Int) {
    data object Success : SignInResult(1)
    data object Fail : SignInResult(2)
    data object DuplicateEmail : SignInResult(3)
}

data class GeoCodeData(
    val addresses: List<AddressData>,
    val errorMessage: String,
    val meta: MetaData,
    val status: String
)

data class AddressData(
    val addressElements: List<AddressElementData>,
    val distance: Double,
    val englishAddress: String,
    val jibunAddress: String,
    val roadAddress: String,
    val x: String,
    val y: String
)

data class MetaData(
    val count: Int,
    val page: Int,
    val totalCount: Int
)

data class AddressElementData(
    val code: String,
    val longName: String,
    val shortName: String,
    val types: List<String>
)

data class ReverseGeoCodeData(
    val results: List<Result>,
    val status: Status
)

data class Result(
    val code: Code,
    val land: Land,
    val name: String,
    val region: Region
)

data class Code(
    val id: String,
    val mappingId: String,
    val type: String
)

data class Land(
    val addition0: Addition,
    val addition1: Addition,
    val addition2: Addition,
    val addition3: Addition,
    val addition4: Addition,
    val coords: Coords,
    val name: String,
    val number1: String,
    val number2: String,
    val type: String
)

data class Addition(
    val type: String,
    val value: String
)

data class Coords(
    val center: Center
)

data class Center(
    val crs: String,
    val x: Double,
    val y: Double
)

data class Region(
    val area0: Area0,
    val area1: Area1,
    val area2: Area0,
    val area3: Area0,
    val area4: Area0
)

data class Area0(
    val coords: Coords,
    val name: String
)

data class Area1(
    val alias: String,
    val coords: Coords,
    val name: String
)

data class Status(
    val code: Int,
    val message: String,
    val name: String
)

data class SearchData(
    val items: List<PlaceItem>
)

data class PlaceItem(
    val title: String,
    val link: String,
    val category: String,
    val description: String,
    val address: String,
    val roadAddress: String,
    val x: Double,
    val y: Double,
    val distance: Double = 0.0
)
