package com.domain.repository

import com.domain.model.ChatMessageData
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

    suspend fun requestFriend(email: String): Int

    suspend fun responseFriendRequest(email: String, value: Boolean): Boolean

    fun getAlarmListFlow(): Flow<List<AlarmData>>

    suspend fun deleteAlarmMessage(time: String): Boolean

    suspend fun getChatCode(email: String): String?

    suspend fun getNewChatCode(): String?

    suspend fun writeChatCode(email: String, code: String): Boolean

    suspend fun getChatData(code: String): List<ChatMessageData>

    fun collectChatData(code: String): Flow<ChatMessageData>

    suspend fun sendChatMessage(message: String, code: String)

    suspend fun getChatMemberEmail(code: String): String?

    fun collectChatRoomData(email: String): Flow<Pair<String, String>>

    suspend fun requestSchedule(
        email: String,
        dateTime: String,
        scheduleAddress: String
    ): Boolean
}