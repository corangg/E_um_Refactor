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