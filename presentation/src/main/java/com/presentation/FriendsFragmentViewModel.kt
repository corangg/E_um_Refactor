package com.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetFriendListUseCase
import com.domain.usecase.GetFriendRequestDataFlow
import com.domain.usecase.GetResponseFriendRequestDataFlow
import com.domain.usecase.GetUserInfoDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FriendsFragmentViewModel @Inject constructor(
    getFriendListUseCase: GetFriendListUseCase,
    getFriendRequestDataFlow: GetFriendRequestDataFlow,
    getUserInfoDataFlowUseCase: GetUserInfoDataFlowUseCase,
    getResponseFriendRequestDataFlow: GetResponseFriendRequestDataFlow,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val friendListData = getFriendListUseCase().asLiveData(viewModelScope.coroutineContext)
    val userNicknameLiveData = getUserInfoDataFlowUseCase().map { it.nickname }.asLiveData(viewModelScope.coroutineContext)
    val userStatusMessageLiveData = getUserInfoDataFlowUseCase().map { it.statusMessage }.asLiveData(viewModelScope.coroutineContext)
    val userProfileUrlLiveData = getUserInfoDataFlowUseCase().map { it.imgUrl }.asLiveData(viewModelScope.coroutineContext)

    private val friendRequestAlarm = getFriendRequestDataFlow().map { it.size }.asLiveData(viewModelScope.coroutineContext)
    private val responseFriendRequestData = getResponseFriendRequestDataFlow().map { it.size }.asLiveData(viewModelScope.coroutineContext)

    val totalAlarmRequests = MediatorLiveData<Int>().apply {
        addSource(responseFriendRequestData) { value = (it ?: 0) + (friendRequestAlarm.value ?: 0) }
        addSource(friendRequestAlarm) { value = (responseFriendRequestData.value ?: 0) + (it ?: 0) }
    }
}