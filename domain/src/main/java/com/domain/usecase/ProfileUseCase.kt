package com.domain.usecase

import com.domain.model.UserInfo
import com.domain.repository.Repository
import javax.inject.Inject

class GetUserInfoData @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getUserInfo()
}

class UpdateUserInfoData @Inject constructor(
    private val repository: Repository,
    private val setUserInfo: SetUserInfo,
    private val updateUserProfileImage: UpdateUserProfileImage
) {
    suspend operator fun invoke(userInfo: UserInfo): Boolean {
        val firebaseProfileImageUrl = updateUserProfileImage(userInfo.imgUrl)
        val updateUserInfoData = userInfo.copy(imgUrl = firebaseProfileImageUrl)

        return setUserInfo(updateUserInfoData).also {
            if (it) repository.upsertUserInfo(updateUserInfoData)
        }
    }
}