package com.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object TimerUtil {
    private var timerJob: Job? = null

    suspend fun startTimer(
        timeSet: Int,
        ioDispatcher: CoroutineDispatcher,
        callback: suspend () -> Unit
    ) {
        timerJob?.cancelAndJoin()
        timerJob = CoroutineScope(ioDispatcher).launch {
            var timer = 0
            while (timer < timeSet) {
                timer += 1000
                delay(1000)
            }
            if (timer == timeSet) {
                callback()
            }
        }
    }

    suspend fun stopTimer() {
        timerJob?.cancelAndJoin()
        timerJob = null
    }
}