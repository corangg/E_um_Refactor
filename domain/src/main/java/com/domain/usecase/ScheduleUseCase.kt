package com.domain.usecase

import com.domain.model.ScheduleData
import com.domain.model.StartEndCoordinate
import com.domain.repository.FirebaseRepository
import com.domain.repository.Repository
import javax.inject.Inject

class GetPublicTransportTimeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(coordinate: StartEndCoordinate, startTime: String): String {
        val time = (repository.getPublicTransPortTime(coordinate, startTime) ?: 0) / 60
        return "${time / 60}시간 ${time % 60}분"
    }
}

class GetCarTimeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(coordinate: StartEndCoordinate): String {
        val time = (repository.getCarTime(coordinate) ?: 0) / 60
        return "${time / 60}시간 ${time % 60}분"
    }
}

class GetWalkTimeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(coordinate: StartEndCoordinate): String {
        val time = (repository.getWalkTime(coordinate) ?: 0) / 60
        return "${time / 60}시간 ${time % 60}분"
    }
}

class RequestScheduleUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(
        email: String,
        dateTime: String,
        scheduleAddress: String
    ) = firebaseRepository.requestSchedule(email, dateTime, scheduleAddress)
}

class CheckScheduleUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(time: String): Boolean {
        return repository.getScheduleData(time) == null
    }
}

class AddScheduleUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(scheduleData: ScheduleData) = repository.upsertScheduleData(scheduleData)
}

class ResponseScheduleUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(
        email: String,
        time: String,
        result: Boolean
    ):Boolean {
        return if (firebaseRepository.responseScheduleRequest(email, result)){
            firebaseRepository.deleteAlarmMessage(time)
        }else{
            false
        }
    }
}