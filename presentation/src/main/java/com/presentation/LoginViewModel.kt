package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.TrySignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val trySignIn: TrySignIn,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val loginEmail = MutableLiveData("")
    val loginPassword = MutableLiveData("")

    val signInResult = MutableLiveData(-1)

    fun tryFirebaseSignIn() = onUiWork {
        val email = loginEmail.value ?: return@onUiWork
        val password = loginPassword.value ?: return@onUiWork
        signInResult.value = trySignIn(email, password)
    }
}