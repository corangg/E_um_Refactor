package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetFriendListUseCase
import com.domain.usecase.GetAlarmListFlow
import com.domain.usecase.GetUserInfoDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FriendsFragmentViewModel @Inject constructor(
    getFriendListUseCase: GetFriendListUseCase,
    getAlarmListFlow: GetAlarmListFlow,
    getUserInfoDataFlowUseCase: GetUserInfoDataFlowUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val friendListData = getFriendListUseCase().asLiveData(viewModelScope.coroutineContext)
    val userNicknameLiveData = getUserInfoDataFlowUseCase().map { it.nickname }.asLiveData(viewModelScope.coroutineContext)
    val userStatusMessageLiveData = getUserInfoDataFlowUseCase().map { it.statusMessage }.asLiveData(viewModelScope.coroutineContext)
    val userProfileUrlLiveData = getUserInfoDataFlowUseCase().map { it.imgUrl }.asLiveData(viewModelScope.coroutineContext)
    val alarmListLiveData = getAlarmListFlow().map { it.size }.asLiveData(viewModelScope.coroutineContext)
}