package com.domain.usecase

import com.domain.model.FriendItemData
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
import javax.inject.Inject


class GetFriendListUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getFriendListFlow()
}

class UpsertFriendListUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val repository: Repository
) {
    suspend operator fun invoke() {
        val friendList = firebaseRepository.getFriendList()
        friendList.map { email ->
            val userInfo = firebaseRepository.getUserInfo() ?: return@map
            val friendItemData = FriendItemData(
                nickName = userInfo.nickname,
                statusMessage = userInfo.statusMessage,
                profileUrl = userInfo.imgUrl
            )
            repository.upsertFriendData(email, friendItemData)
        }
    }
}

class DeleteAllFriendListUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.deleteAllFriendData()
}

class GetFriendRequestDataFlow @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.getFirebaseRequestFriendAlarmData()
}