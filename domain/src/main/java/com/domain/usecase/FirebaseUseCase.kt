package com.domain.usecase

import com.domain.model.AddressItemData
import com.domain.model.FriendRequestResult
import com.domain.model.SignInResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
import javax.inject.Inject

class CheckLoggedInUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.isUserLoggedIn()
}

class TrySignUpUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) =
        firebaseRepository.trySignUp(userInfo.email, userInfo.password)
}

class SetUserInfoUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(userInfo: UserInfo) = firebaseRepository.setUserData(userInfo)
}

class TrySignInUseCase @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository,
    private val getFireBaseUserInfoUseCase: GetFireBaseUserInfoUseCase,
    private val upsertAddressDataUseCase: UpsertAddressDataUseCase
) {
    suspend operator fun invoke(email: String, password: String): Int {
        val signInResult = firebaseRepository.trySignIn(email, password).code
        if (signInResult == SignInResult.Success.code) {
            val fireBaseUserInfo = getFireBaseUserInfoUseCase() ?: return SignInResult.UserNotFound.code
            repository.upsertUserInfo(fireBaseUserInfo)
            upsertAddressDataUseCase(AddressItemData(
                address = fireBaseUserInfo.address,
                zoneCode = fireBaseUserInfo.zoneCode,
                mainValue = true
            ))
        }
        return signInResult
    }
}

class GetFireBaseUserInfoUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke() = firebaseRepository.getUserInfo()
}

class UpdateUserProfileImageUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(uri: String) = firebaseRepository.updateProfileImage(uri)
}

class CheckExistingPasswordUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(password: String) = firebaseRepository.checkPassword(password)
}

class ChangePasswordUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(password: String) = firebaseRepository.changePassword(password)
}

class TrySignOutUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke() = firebaseRepository.trySignOut()
}

class GetFireBaseEmailInfoUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String) = firebaseRepository.getEmailInfo(email)
}

class TryFriendRequestUseCase @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(email: String): Int {
        return if (repository.getFriendList().any { it.email.contains(email) }) {
            FriendRequestResult.DuplicateEmail.code
        } else {
            firebaseRepository.requestFriend(email)
        }
    }
}