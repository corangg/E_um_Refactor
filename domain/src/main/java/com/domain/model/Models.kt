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
