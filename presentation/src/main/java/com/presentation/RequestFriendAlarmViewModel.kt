package com.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetFriendRequestDataFlow
import com.domain.usecase.ResponseFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class RequestFriendAlarmViewModel @Inject constructor(
    getFriendRequestDataFlow: GetFriendRequestDataFlow,
    private val responseFriendRequestUseCase: ResponseFriendRequestUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val friendRequestAlarm = getFriendRequestDataFlow().asLiveData(viewModelScope.coroutineContext)

    val onResponseResult = MutableLiveData(false)

    fun responseFriendRequest(position: Int, value: Boolean) = onUiWork {
        val email = friendRequestAlarm.value?.getOrNull(position) ?: return@onUiWork
        onResponseResult.value = responseFriendRequestUseCase(email, value)
    }
}