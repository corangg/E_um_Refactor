package com.domain.usecase

import com.domain.model.AddressItemData
import com.domain.model.UserInfo
import com.domain.repository.Repository
import javax.inject.Inject

class GetUserInfoDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getUserInfo()
}

class UpdateUserInfoDataUseCase @Inject constructor(
    private val repository: Repository,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val updateUserProfileImageUseCase: UpdateUserProfileImageUseCase,
    private val updateMainAddressUseCase: UpdateMainAddressUseCase
) {
    suspend operator fun invoke(userInfo: UserInfo, mainAddressPosition: Int): Boolean {
        val updateUserInfoData = userInfo.copy(imgUrl = updateUserProfileImageUseCase(userInfo.imgUrl))

        return setUserInfoUseCase(updateUserInfoData).apply {
            if (this) {
                if (mainAddressPosition > -1) updateMainAddressUseCase(mainAddressPosition)
                repository.upsertUserInfo(updateUserInfoData)
            }
        }
    }
}

class GetUserInfoDataFlowUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getUserInfoFlow()
}