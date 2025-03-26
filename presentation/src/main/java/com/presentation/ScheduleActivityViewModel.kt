package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetUserInfoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ScheduleActivityViewModel @Inject
constructor(
    private val getUserInfoDataUseCase: GetUserInfoDataUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val dateText = MutableLiveData("")
    val isAmPm = MutableLiveData(true)
    val textScheduleHour = MutableLiveData("00")
    val textScheduleMinute = MutableLiveData("00")
    val textAlarmHour = MutableLiveData("00")
    val textAlarmMinute = MutableLiveData("00")
    val textStartLocation = MutableLiveData("")
    val textScheduleLocation = MutableLiveData("")

    init {
        setStartLocation()
    }

    private fun setStartLocation() = onUiWork {
        textStartLocation.value = getUserInfoDataUseCase()?.address
    }

    fun setDateText(date: String) = onUiWork {
        dateText.value = date
    }

    fun onAmPmButton(value: Boolean) = onUiWork {
        isAmPm.value = value
    }

    fun changeStartAddress(address: String){
        textStartLocation.value = address
    }

 /*   fun onSetStartLocation() = onUiWork{

    }

    fun onSetScheduleLocation() = onUiWork{

    }*/

    fun onCheckTransport(type: Int) = onUiWork {

    }

    fun onOk() =onUiWork {

    }

    fun onNo() = onUiWork {

    }
}