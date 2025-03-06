package com.domain.usecase

import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import javax.inject.Inject

class CheckLoggedIn @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.isUserLoggedIn()
}

class TrySignUp @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) =
        firebaseRepository.trySignUp(userInfo.email, userInfo.password)
}

class SetUserInfo @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) = firebaseRepository.setUserData(userInfo)
}

class TrySignIn @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String, password: String) =
        firebaseRepository.trySignIn(email, password).code
}

class GetProfileInfo @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke() = firebaseRepository.getUserInfo()
}