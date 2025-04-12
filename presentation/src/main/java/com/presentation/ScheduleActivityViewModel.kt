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
import com.domain.model.ScheduleData
import com.domain.model.ScheduleResult
import com.domain.model.StartEndCoordinate
import com.domain.usecase.AddScheduleUseCase
import com.domain.usecase.CheckScheduleUseCase
import com.domain.usecase.GetCarTimeUseCase
import com.domain.usecase.GetCoordinateToAddressUseCase
import com.domain.usecase.GetPublicTransportTimeUseCase
import com.domain.usecase.GetScheduleDataUseCase
import com.domain.usecase.GetUserInfoDataUseCase
import com.domain.usecase.GetWalkTimeUseCase
import com.domain.usecase.RequestScheduleUseCase
import com.domain.usecase.ResponseScheduleUseCase
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
    private val checkScheduleUseCase: CheckScheduleUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val responseScheduleUseCase: ResponseScheduleUseCase,
    private val getScheduleDataUseCase: GetScheduleDataUseCase,
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
    val scheduleResult = MutableLiveData<Int>()

    init {
        setStartLocation()
    }

    private fun setStartLocation() = onUiWork {
        textStartLocation.value = getUserInfoDataUseCase()?.address
    }

    fun setDateText(date: String) = onUiWork {
        dateText.value = date
    }

    fun setTime(time: String) = onUiWork {
        var hour = time.substring(0, 2).toIntOrNull() ?: 0
        val min = time.substring(2, 4).toIntOrNull() ?: 0
        if (hour >= 12) {
            hour -= 12
            isAmPm.value = false
        }
        val editTime = toHHMMFormat(hour, min)
        textScheduleHour.value = editTime.substring(0,2)
        textScheduleMinute.value = editTime.substring(2,4)
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

    fun setSchedule(time: String) = onUiWork {
        val scheduleData = getScheduleDataUseCase(time)?: return@onUiWork
        changeStartAddress(scheduleData.startAddress)
        changeScheduleAddress(scheduleData.scheduleAddress)
        setDateText(scheduleData.time.substring(0,8))
        setTime(scheduleData.time.substring(8,12))
        setTransportType(scheduleData.transportType)
        setAlarmTime(scheduleData.alarmTime)
    }

    private fun setTransportType(type: Int) = onUiWork {
        selectTransportType.value = type
    }

    private fun setAlarmTime(alarmTime: String) {
        textAlarmHour.value = alarmTime.substring(0, 2)
        textAlarmMinute.value = alarmTime.substring(2, 4)
    }

    private fun getDateTime(): String {
        val date = dateText.value?.replace("-", "") ?: "00000000"
        val time = if (isAmPm.value == false) {
            val hour = 12 + (textScheduleHour.value?.toIntOrNull() ?: 0)
            val min = textScheduleMinute.value?.toIntOrNull() ?: 0
            toHHMMFormat(hour, min)
        } else {
            val hour = (textScheduleHour.value?.toIntOrNull() ?: 0)
            val min = textScheduleMinute.value?.toIntOrNull() ?: 0
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

    fun requestSchedule(email: String, nickname: String) = onUiWork {
        val scheduleTime = getDateTime()
        if (!checkScheduleUseCase(scheduleTime)) {
            scheduleResult.value = ScheduleResult.DuplicationSchedule.type
            return@onUiWork
        }
        val requestResult = requestScheduleUseCase(
            email,
            getDateTime(),
            textScheduleLocation.value ?: return@onUiWork
        )
        if(requestResult){
            addSchedule(email, nickname, false)
            scheduleResult.value = ScheduleResult.Request.type
        }else{
            scheduleResult.value = ScheduleResult.Fail.type
        }
    }

    private fun addSchedule(email: String, nickname: String, type: Boolean) = onIoWork {
        val scheduleData = ScheduleData(
            time = getDateTime(),
            email = email,
            nickname = nickname,
            startAddress = textStartLocation.value ?: "",
            scheduleAddress = textScheduleLocation.value ?: "",
            alarmTime = getAlarmTime(),
            transportType = selectTransportType.value ?: 1,
            requestValue = type
        )
        addScheduleUseCase(scheduleData)
    }

    fun onOk(email: String, nickname: String, time: String) = onUiWork {
        addSchedule(email, nickname, true)
        responseScheduleUseCase(email, time, true, getDateTime())
        scheduleResult.value = ScheduleResult.Accept.type
    }

    fun onNo(email: String, time: String) = onUiWork {
        responseScheduleUseCase(email,time,false, getDateTime())
    }

    fun onSave(time: String) = onUiWork {
        val scheduleData = getScheduleDataUseCase(time)?:return@onUiWork
        addSchedule(scheduleData.email, scheduleData.nickname, true)
    }
}