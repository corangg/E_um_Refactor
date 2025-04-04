package com.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.StartEndCoordinate
import com.domain.usecase.GetCarTimeUseCase
import com.domain.usecase.GetCoordinateToAddressUseCase
import com.domain.usecase.GetPublicTransportTimeUseCase
import com.domain.usecase.GetUserInfoDataUseCase
import com.domain.usecase.GetWalkTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ScheduleActivityViewModel @Inject
constructor(
    private val getUserInfoDataUseCase: GetUserInfoDataUseCase,
    private val getCoordinateToAddressUseCase: GetCoordinateToAddressUseCase,
    private val getPublicTransportTimeUseCase: GetPublicTransportTimeUseCase,
    private val getCarTimeUseCase: GetCarTimeUseCase,
    private val getWalkTimeUseCase: GetWalkTimeUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val dateText = MutableLiveData("")
    val isAmPm = MutableLiveData(true)
    val textScheduleHour = MutableLiveData("00")
    val textScheduleMinute = MutableLiveData("00")
    val textAlarmHour = MutableLiveData("00")
    val textAlarmMinute = MutableLiveData("30")
    val textStartLocation = MutableLiveData("")
    private val _textScheduleLocation = MutableLiveData<String>()
    val textScheduleLocation: LiveData<String> = _textScheduleLocation.distinctUntilChanged()
    val selectTransportType = MutableLiveData(-1)
    val textPublicTransportTime = MutableLiveData("00시간 00분")
    val textCarTime = MutableLiveData("00시간 00분")
    val textWalkTime = MutableLiveData("00시간 00분")

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

    fun changeStartAddress(address: String) = onUiWork {
        textStartLocation.value = address
    }

    fun changeScheduleAddress(address: String) = onUiWork {
        _textScheduleLocation.value = address
    }

    private fun getTime(): String {
        val date = dateText.value?.replace("-", "") ?: "00000000"
        val time = if (isAmPm.value == false) {
            val hour = 12 + (textScheduleHour.value?.toInt() ?: 0)
            "${hour}${textScheduleMinute.value}"
        } else {
            "${textScheduleHour.value}${textScheduleMinute.value}"
        }
        return "$date$time"
    }

    fun setTransportTime() = onUiWork {
        val startLocation = textStartLocation.value?:return@onUiWork
        val scheduleLocation = textScheduleLocation.value?:return@onUiWork
        val startCoordinate = getCoordinateToAddressUseCase(startLocation)
        val scheduleCoordinate = getCoordinateToAddressUseCase(scheduleLocation)
        val startEndCoordinate = StartEndCoordinate(
            startCoordinate.first,
            startCoordinate.second,
            scheduleCoordinate.first,
            scheduleCoordinate.second
        )
        setPublicTransportTime(startEndCoordinate)
        setCarTime(startEndCoordinate)
        setWalkTime(startEndCoordinate)
    }

    private fun setPublicTransportTime(coordinate: StartEndCoordinate) = onUiWork{
        textPublicTransportTime.value = getPublicTransportTimeUseCase(coordinate, getTime())
    }

    private fun setCarTime(coordinate: StartEndCoordinate) = onUiWork {
        textCarTime.value = getCarTimeUseCase(coordinate, getTime())
    }

    private fun setWalkTime(coordinate: StartEndCoordinate) = onUiWork {
        textWalkTime.value = getWalkTimeUseCase(coordinate)
    }


    fun selectTransport(type: Int) = onUiWork {
        selectTransportType.value = type
    }

    fun onOk() = onUiWork {

    }

    fun onNo() = onUiWork {

    }
}