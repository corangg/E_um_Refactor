package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AddFriendActivityViewModel @Inject constructor(
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val searchEmailLiveData = MutableLiveData("")
    val searchNickNameLiveData = MutableLiveData("")
    val searchStatusMessageLiveData = MutableLiveData("")
    val profileImageUrl = MutableLiveData("")

    val onShowRequestView = MutableLiveData(false)
    val onFriendRequestValue = MutableLiveData(-1)

    fun searchFriend() = onUiWork {

    }

    fun requestFriend() = onUiWork {

    }
}