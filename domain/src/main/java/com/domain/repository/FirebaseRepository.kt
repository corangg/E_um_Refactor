package com.domain.repository

import com.domain.model.AlarmData
import com.domain.model.SignInResult
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun isUserLoggedIn(): Flow<Boolean>

    suspend fun trySignUp(email: String, password: String): SignUpResult

    suspend fun setUserData(userInfo: UserInfo): Boolean

    suspend fun trySignIn(email: String, password: String): SignInResult

    suspend fun getUserInfo(): UserInfo?

    suspend fun updateProfileImage(uri: String): String

    suspend fun checkPassword(password: String): Boolean

    suspend fun changePassword(password: String): Boolean

    suspend fun trySignOut()

    suspend fun getFriendList(): List<String>

    suspend fun getEmailInfo(email: String): UserInfo?

    suspend fun updateFriendValue(email: String): Boolean

    suspend fun requestFriend(email: String): Boolean

    suspend fun responseFriendRequest(email: String, value: Boolean): Boolean

    fun getAlarmListFlow(): Flow<List<AlarmData>>

    suspend fun deleteAlarmMessage(time: String): Boolean
}