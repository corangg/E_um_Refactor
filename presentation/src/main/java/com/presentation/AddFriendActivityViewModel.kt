package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetFireBaseEmailInfoUseCase
import com.domain.usecase.TryFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AddFriendActivityViewModel @Inject constructor(
    private val getFireBaseEmailInfoUseCase: GetFireBaseEmailInfoUseCase,
    private val tryFriendRequestUseCase: TryFriendRequestUseCase,
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
        val emailInfo = getFireBaseEmailInfoUseCase(searchEmailLiveData.value ?: return@onUiWork)
        if(emailInfo != null){
            searchNickNameLiveData.value = emailInfo.nickname
            searchStatusMessageLiveData.value = emailInfo.statusMessage
            profileImageUrl.value = emailInfo.imgUrl
            onShowRequestView.value = true
        }
    }

    fun requestFriend() = onUiWork {
        onFriendRequestValue.value = if (tryFriendRequestUseCase(searchEmailLiveData.value ?: "")) 1 else 2
    }
}