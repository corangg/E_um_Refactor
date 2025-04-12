package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetAlarmListFlow
import com.domain.usecase.GetScheduleListFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ScheduleFragmentViewModel @Inject constructor(
    getScheduleListFlowUseCase: GetScheduleListFlowUseCase,
    getAlarmListFlow: GetAlarmListFlow,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val scheduleListData = getScheduleListFlowUseCase().asLiveData(viewModelScope.coroutineContext)
    val alarmListLiveData = getAlarmListFlow().map { it.size }.asLiveData(viewModelScope.coroutineContext)
}