package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.GetProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val getUserInfo: GetProfileInfo,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val profileNickname = MutableLiveData("")
    val profileStatusMessage = MutableLiveData("")
    val profileName = MutableLiveData("")
    val profilePhone = MutableLiveData("")
    val profileEmail = MutableLiveData("")
    val profilePassword = MutableLiveData("")
    val profileAddress = MutableLiveData("")
    val profileImageUrl = MutableLiveData(null)

    init {
        setProfileData()
    }

    private fun setProfileData() = onUiWork {
        val userInfo = getUserInfo() ?: return@onUiWork
        profileNickname.value = userInfo.nickname
        profileStatusMessage.value = userInfo.statusMessage
        profileName.value = userInfo.name
        profilePhone.value = userInfo.phone.toString()
        profileEmail.value = userInfo.email
        profilePassword.value = userInfo.password
        profileAddress.value = "${userInfo.address}, ${userInfo.zoneCode}"
        profileAddress.value = userInfo.imgUrl
    }
}