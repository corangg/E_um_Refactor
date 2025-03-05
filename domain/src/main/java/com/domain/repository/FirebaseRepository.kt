package com.domain.repository

import com.domain.model.SignUpResult
import com.domain.model.UserInfo

interface FirebaseRepository {
    suspend fun trySignUp(email: String, password: String): SignUpResult

    suspend fun setUserData(userInfo: UserInfo): Boolean
}