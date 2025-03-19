package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.ChatMessageData
import com.domain.usecase.GetChatList
import com.domain.usecase.StartChatUseCase
import com.domain.usecase.SendChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ChatActivityViewModel @Inject constructor(
    private val startChatUseCase: StartChatUseCase,
    private val getChatList: GetChatList,
    private val sendChatUseCase: SendChatUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val chatMessageList = MutableLiveData(listOf<ChatMessageData>())
    val chatText = MutableLiveData("")

    fun getChatData(email: String) = onUiWork{
        startChatUseCase(email)
    }

    fun test(email: String)= onUiWork {
        getChatList(email).collect{
            chatMessageList.value = it
        }
    }

    fun sendChat(email: String) = onUiWork {
        val chatMessage = chatText.value?: return@onUiWork
        sendChatUseCase(email, chatMessage)
        chatText.value = ""
    }
}