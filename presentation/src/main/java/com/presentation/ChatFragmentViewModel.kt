package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.CollectChatRoomDataUseCase
import com.domain.usecase.GetChatListUseCase
import com.domain.usecase.StartChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ChatFragmentViewModel @Inject constructor(
    getChatListUseCase: GetChatListUseCase,
    private val collectChatRoomDataUseCase: CollectChatRoomDataUseCase,
    private val startChatUseCase: StartChatUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val chatRoomList = getChatListUseCase().asLiveData(viewModelScope.coroutineContext)

    fun getChatRoomData() = onUiWork {
        collectChatRoomDataUseCase()
    }
}