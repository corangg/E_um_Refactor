package com.domain.usecase

import com.domain.model.SignInResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
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

class TrySignIn @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository,
    private val getFireBaseUserInfo: GetFireBaseUserInfo
) {
    suspend operator fun invoke(email: String, password: String): Int {
        val signInResult = firebaseRepository.trySignIn(email, password).code
        if (signInResult == SignInResult.Success.code) {
            val fireBaseUserInfo = getFireBaseUserInfo() ?: return SignInResult.UserNotFound.code
            repository.upsertUserInfo(fireBaseUserInfo)
        }
        return signInResult
    }
}

class GetFireBaseUserInfo @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke() = firebaseRepository.getUserInfo()
}

class UpdateUserProfileImage @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(uri: String) = firebaseRepository.updateProfileImage(uri)
}

class CheckExistingPassword @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(password: String) = firebaseRepository.checkPassword(password)
}

class ChangePassword @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(password: String) = firebaseRepository.changePassword(password)
}