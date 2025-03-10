package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.UserInfo
import com.domain.usecase.DeleteAllAddressUseCase
import com.domain.usecase.GetChoiceAddressDataUseCase
import com.domain.usecase.GetUserInfoDataUseCase
import com.domain.usecase.TrySignOutUseCase
import com.domain.usecase.UpdateUserInfoDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoDataUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoDataUseCase,
    private val trySignOutUseCase: TrySignOutUseCase,
    private val deleteAllAddressUseCase: DeleteAllAddressUseCase,
    private val getChoiceAddressDataUseCase: GetChoiceAddressDataUseCase,
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

    private var mainAddressPosition = -1

    init {
        initProfileData()
    }

    private fun initProfileData() = onUiWork {
        val userInfo = getUserInfoUseCase() ?: return@onUiWork
        profileNickname.value = userInfo.nickname
        profileStatusMessage.value = userInfo.statusMessage
        profileName.value = userInfo.name
        profilePhone.value = userInfo.phone
        profileEmail.value = userInfo.email
        profilePassword.value = userInfo.password
        profileAddress.value = "${userInfo.zoneCode}, ${userInfo.address}"
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
            address = addressData.drop(1).joinToString(","),
            zoneCode = addressData.firstOrNull() ?: "",
            imgUrl = profileImageUrl.value ?: ""
        )
        successUerInfoEdit.value = updateUserInfoUseCase(userInfo, mainAddressPosition)
    }

    fun setImageProfileUri(uri: String) {
        profileImageUrl.value = uri
    }

    fun setPassword(password: String) {
        profilePassword.value = password
    }

    fun setAddress(position: Int) = onUiWork {
        mainAddressPosition = position
        val zonCode = getChoiceAddressDataUseCase(position).zoneCode
        val address = getChoiceAddressDataUseCase(position).address
        profileAddress.value = "$zonCode, $address"
    }

    fun trySignOut() = onUiWork {
        deleteAllAddressUseCase()
        trySignOutUseCase()
    }
}