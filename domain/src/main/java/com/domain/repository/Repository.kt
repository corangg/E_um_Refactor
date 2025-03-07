package com.domain.repository

import com.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getUserInfo(): UserInfo?

    suspend fun upsertUserInfo(userInfo: UserInfo)

    fun getUserInfoFlow(): Flow<UserInfo>
}