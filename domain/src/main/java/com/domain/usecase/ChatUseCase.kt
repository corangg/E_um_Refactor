package com.domain.usecase

import com.domain.model.ChatMessageData
import com.domain.model.ChatRoomItemData
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * 상대 Email 과 있는 채팅의 ChatCode 를 반환 하는 UseCase
 * 채팅이 존재 하지 않으면 새로운 ChatCode 를 생성 후 반환
 **/
class GetChatCodeUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String) = firebaseRepository.getChatCode(email) ?: createChatCode(email)

    private suspend fun createChatCode(email: String): String? {
        val newCode = firebaseRepository.getNewChatCode() ?: return null
        firebaseRepository.writeChatCode(email, newCode)
        return newCode
    }
}

/** ChatCode 를 토대로 상대방 정보를 반환 하는 UseCase **/
class GetOpponentInfoUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(code: String): UserInfo? {
        val getOpponentEmail = firebaseRepository.getChatMemberEmail(code) ?: return null
        return firebaseRepository.getEmailInfo(getOpponentEmail)
    }
}

/** ChatCode 에 해당 하는 채팅 정보를 Room 에 저장 하는 UseCase **/
class StartChatUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val repository: Repository
) {
    suspend operator fun invoke(code: String) {
        firebaseRepository.collectChatData(code).collect {
            repository.updateChatData(it, code)
        }
    }
}

/** ChatCode 에 해당 하는 채팅 정보를 반환 하는 UseCase **/
class GetChatValueUseCase @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(code: String): Flow<List<ChatMessageData>> = flow {
        val opponentEmail = firebaseRepository.getChatMemberEmail(code) ?: return@flow
        val friendData = firebaseRepository.getEmailInfo(opponentEmail) ?: return@flow

        val chatListFlow = repository.getChatList(code).mapNotNull { messageData ->
            messageData?.chatList?.mapIndexed { index, current ->
                if (current.email == opponentEmail) {
                    if (index > 0 && messageData.chatList[index - 1].email == current.email) {
                        current.copy(myMessage = false, imgUrl = "", messageType = 2)
                    } else {
                        current.copy(myMessage = false, imgUrl = friendData.imgUrl, messageType = 3)
                    }
                } else {
                    current
                }
            }
        }
        chatListFlow.collect {
            emit(it)
        }
    }
}

/** ChatCode 에 해당 하는 채팅 정보를 전송 하는 UseCase **/
class SendChatUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(code: String, message: String) {
        firebaseRepository.sendChatMessage(message, code)
    }
}






class DeleteChatUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        repository.deleteChat()
    }
}

class GetChatListUseCase @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<List<ChatRoomItemData>> = flow {
        val chatDataListFlow = repository.getAllChatData()
        val chatRoomData = chatDataListFlow.mapNotNull { chatDataList ->
            chatDataList.map { chatData ->
                val opponentEmail = firebaseRepository.getChatMemberEmail(chatData.chatCode) ?: return@mapNotNull null
                val opponentInfo = firebaseRepository.getEmailInfo(opponentEmail) ?: return@mapNotNull null
                val notReadCount = chatData.chatList.count { !it.isRead }
                ChatRoomItemData(
                    chatCode = chatData.chatCode,
                    name = opponentInfo.nickname,
                    imgUrl = opponentInfo.imgUrl,
                    notReadCount = notReadCount,
                    lastMessage = chatData.chatList.lastOrNull()?.message ?: ""
                )
            }
        }
        chatRoomData.collect{
            emit(it)
        }
    }
}