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
