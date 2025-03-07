package com.domain.usecase

import com.domain.model.UserInfo
import com.domain.repository.Repository
import javax.inject.Inject

class GetUserInfoData @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getUserInfo()
}
class UpsertUserInfoData @Inject constructor(
    private val repository: Repository,
    private val setUserInfo: SetUserInfo
) {
    suspend operator fun invoke(userInfo: UserInfo): Boolean {
        return setUserInfo(userInfo).also {
            if (it) repository.upsertUserInfo(userInfo)
        }
    }
}