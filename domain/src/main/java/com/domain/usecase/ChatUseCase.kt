package com.domain.usecase

import com.domain.model.ChatMessageData
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class StartChatUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val repository: Repository
) {
    suspend operator fun invoke(email: String) {
        val chatCode = firebaseRepository.getChatCode(email) ?: getNewChatCode(email) ?: return
        firebaseRepository.collectChatData(chatCode).collect {
            repository.updateChatData(it, chatCode)
        }
    }

    private suspend fun getNewChatCode(email: String): String? {
        val newCode = firebaseRepository.getNewChatCode() ?: return null
        firebaseRepository.writeChatCode(email, newCode)
        return newCode
    }
}

class GetChatList @Inject constructor(
    private val repository: Repository,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(email: String): Flow<List<ChatMessageData>> = flow {
        val chatCode = firebaseRepository.getChatCode(email) ?: return@flow
        val friendData = firebaseRepository.getEmailInfo(email) ?: return@flow
        val chatListFlow = repository.getChatList(chatCode).mapNotNull { messageData ->
            messageData?.chatList?.mapIndexed { index, current ->
                if (current.email == email) {
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

class SendChatUseCase @Inject constructor(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String, message: String) {
        val chatCode = firebaseRepository.getChatCode(email) ?: return
        firebaseRepository.sendChatMessage(message, chatCode)
    }
}

class DeleteChatUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        repository.deleteChat()
    }
}