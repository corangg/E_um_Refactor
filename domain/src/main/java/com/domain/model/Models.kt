package com.domain.model

data class UserInfo(
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phone: Int,
    val zoneCode: Int,
    val address: String,
)