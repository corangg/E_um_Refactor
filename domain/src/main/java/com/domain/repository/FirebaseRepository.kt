package com.domain.repository

import com.domain.model.SignInResult
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun isUserLoggedIn(): Flow<Boolean>

    suspend fun trySignUp(email: String, password: String): SignUpResult

    suspend fun setUserData(userInfo: UserInfo): Boolean

    suspend fun trySignIn(email: String, password: String): SignInResult
}