package com.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.util.subtractHHMMFromDateTime
import com.core.util.toHHMMFormat
import com.core.viewmodel.BaseViewModel
import com.domain.model.StartEndCoordinate
import com.domain.usecase.GetCarTimeUseCase
import com.domain.usecase.GetCoordinateToAddressUseCase
import com.domain.usecase.GetPublicTransportTimeUseCase
import com.domain.usecase.GetUserInfoDataUseCase
import com.domain.usecase.GetWalkTimeUseCase
import com.domain.usecase.RequestScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val requestScheduleUseCase: RequestScheduleUseCase,
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
    val selectTransportType = MutableLiveData(1)
    val textPublicTransportTime = MutableLiveData("00시간 00분")
    val textCarTime = MutableLiveData("00시간 00분")
    val textWalkTime = MutableLiveData("00시간 00분")
    val isRequestResult = MutableLiveData<Boolean>()

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

    private fun getDateTime(): String {
        val date = dateText.value?.replace("-", "") ?: "00000000"
        val time = if (isAmPm.value == false) {
            val hour = 12 + (textScheduleHour.value?.toInt() ?: 0)
            val min = textScheduleMinute.value?.toInt() ?: 0
            toHHMMFormat(hour, min)
        } else {
            val hour = (textScheduleHour.value?.toInt() ?: 0)
            val min = textScheduleMinute.value?.toInt() ?: 0
            toHHMMFormat(hour, min)
        }
        return "$date$time"
    }

    private fun getAlarmTime() = toHHMMFormat((textAlarmHour.value?.toInt() ?: 0), (textAlarmMinute.value?.toInt() ?: 0))

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
        val time = subtractHHMMFromDateTime(getDateTime(), getAlarmTime())
        textPublicTransportTime.value = getPublicTransportTimeUseCase(coordinate, time)
    }

    private fun setCarTime(coordinate: StartEndCoordinate) = onUiWork {
        textCarTime.value = getCarTimeUseCase(coordinate)
    }

    private fun setWalkTime(coordinate: StartEndCoordinate) = onUiWork {
        textWalkTime.value = getWalkTimeUseCase(coordinate)
    }

    fun selectTransport(type: Int) = onUiWork {
        selectTransportType.value = type
    }

    fun requestSchedule(email: String) = onUiWork {
        isRequestResult.value = requestScheduleUseCase(
            email,
            getDateTime(),
            textScheduleLocation.value ?: return@onUiWork
        )
    }

    fun onOk() = onUiWork {

    }

    fun onNo() = onUiWork {

    }
}