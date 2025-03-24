package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.ChatMessageData
import com.domain.usecase.GetChatValueUseCase
import com.domain.usecase.GetOpponentInfoUseCase
import com.domain.usecase.StartChatUseCase
import com.domain.usecase.SendChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ChatActivityViewModel @Inject constructor(
    private val startChatUseCase: StartChatUseCase,
    private val getChatValueUseCase: GetChatValueUseCase,
    private val sendChatUseCase: SendChatUseCase,
    private val getOpponentInfoUseCase: GetOpponentInfoUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val chatMessageList = MutableLiveData(listOf<ChatMessageData>())
    val chatText = MutableLiveData("")
    val chatRoomName = MutableLiveData("")

    fun setChatRoom(code: String) = onUiWork {
        val opponentInfo = getOpponentInfoUseCase(code)?: return@onUiWork
        chatRoomName.value = opponentInfo.nickname
        startChatUseCase(code)
    }

    fun updateChatList(code: String)= onUiWork {
        getChatValueUseCase(code).collect{
            chatMessageList.value = it
        }
    }

    fun sendChat(code: String) = onUiWork {
        val chatMessage = chatText.value?: return@onUiWork
        sendChatUseCase(code, chatMessage)
        chatText.value = ""
    }

    fun viewChat(code: String) = onUiWork {

    }
}