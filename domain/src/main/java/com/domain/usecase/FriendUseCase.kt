package com.domain.usecase

import com.domain.model.AlarmData
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
            val userInfo = firebaseRepository.getEmailInfo(email) ?: return@map
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

class GetAlarmListFlow @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    operator fun invoke() = firebaseRepository.getAlarmListFlow()
}

class ResponseFriendRequestUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val upsertFriendListUseCase: UpsertFriendListUseCase
) {
    suspend operator fun invoke(data: AlarmData.RequestFriendAlarmData, value: Boolean): Boolean {
        val replaceEmail = data.email.replace("_", ".")
        return if (value) {
            firebaseRepository.updateFriendValue(replaceEmail) &&
                    firebaseRepository.deleteAlarmMessage(data.time) &&
                    firebaseRepository.responseFriendRequest(data.email, true)
                        .also { upsertFriendListUseCase() }
        } else {
            firebaseRepository.deleteAlarmMessage(data.email) && firebaseRepository.responseFriendRequest(
                data.email,
                false
            )
        }
    }
}

class DeleteAlarmUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(time: String) = firebaseRepository.deleteAlarmMessage(time)
}