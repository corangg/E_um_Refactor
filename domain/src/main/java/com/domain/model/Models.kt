package com.domain.model

data class UserInfo(
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phone: Int,
    val zoneCode: Int,
    val address: String,
    val imgUrl: String = "",
    val statusMessage: String = "",
    val timeStamp: String = ""
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
