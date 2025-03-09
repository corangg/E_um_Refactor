package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.ChangePassword
import com.domain.usecase.CheckExistingPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val checkExistingPassword: CheckExistingPassword,
    private val changePassword: ChangePassword,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val existingPassword = MutableLiveData("")
    val newPassword = MutableLiveData("")
    val newCheckPassword = MutableLiveData("")

    val passwordEditResult = MutableLiveData(-1)
    private var checkExistingPasswordResult = false

    fun checkExistingPassword() = onUiWork {
        if (checkExistingPassword(existingPassword.value ?: return@onUiWork)) {
            checkExistingPasswordResult = true
            passwordEditResult.value = 1
        } else {
            checkExistingPasswordResult = false
            passwordEditResult.value = 2
        }
    }

    fun editPassword() = onUiWork {
        passwordEditResult.value = when {
            !checkExistingPasswordResult -> 3
            newPassword.value != newCheckPassword.value -> 4
            changePassword(newPassword.value ?: return@onUiWork) -> 5
            else -> 6
        }
    }
}