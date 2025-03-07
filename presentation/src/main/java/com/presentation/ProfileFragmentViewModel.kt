package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.UserInfo
import com.domain.usecase.GetUserInfoData
import com.domain.usecase.UpdateUserInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val getUserInfo: GetUserInfoData,
    private val updateUserInfo: UpdateUserInfoData,
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
    val profileImageUrl = MutableLiveData("")

    val successUerInfoEdit = MutableLiveData(false)

    init {
        initProfileData()
    }

    private fun initProfileData() = onUiWork {
        val userInfo = getUserInfo()?:return@onUiWork
        profileNickname.value = userInfo.nickname
        profileStatusMessage.value = userInfo.statusMessage
        profileName.value = userInfo.name
        profilePhone.value = userInfo.phone
        profileEmail.value = userInfo.email
        profilePassword.value = userInfo.password
        profileAddress.value = "${userInfo.address}, ${userInfo.zoneCode}"
        profileImageUrl.value = userInfo.imgUrl
    }

    fun updateUserInfoData() = onUiWork {
        val addressData = profileAddress.value?.split(",") ?: listOf()
        val userInfo = UserInfo(
            nickname = profileNickname.value ?: "",
            statusMessage = profileStatusMessage.value ?: "",
            name = profileName.value ?: "",
            phone = profilePhone.value ?: "",
            email = profileEmail.value ?: "",
            password = profilePassword.value ?: "",
            address = addressData[0],
            zoneCode = addressData[1],
            imgUrl = profileImageUrl.value ?: ""
        )
        successUerInfoEdit.value = updateUserInfo(userInfo)
    }

    fun setImageProfileUri(uri: String){
        profileImageUrl.value = uri
    }
}