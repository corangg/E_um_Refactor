package com.domain.usecase

import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import javax.inject.Inject

class TrySignUp @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) =
        firebaseRepository.trySignUp(userInfo.email, userInfo.password)
}

class SetUserInfo @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) = firebaseRepository.setUserData(userInfo)
}