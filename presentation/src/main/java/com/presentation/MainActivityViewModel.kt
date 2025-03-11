package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.CheckLoggedInUseCase
import com.domain.usecase.UpsertFriendListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    checkLoggedInUseCase: CheckLoggedInUseCase,
    private val upsertFriendListUseCase: UpsertFriendListUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val checkLoggedInValue = checkLoggedInUseCase().asLiveData(viewModelScope.coroutineContext)

    init {
        onIoWork { upsertFriendListUseCase() }
    }
}