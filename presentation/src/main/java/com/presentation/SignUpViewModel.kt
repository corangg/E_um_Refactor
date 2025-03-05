package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val emailLiveData = MutableLiveData("")
    val passwordLiveData = MutableLiveData("")
    val passwordCheckLiveData = MutableLiveData("")
    val nameLiveData = MutableLiveData("")
    val nicknameLiveData = MutableLiveData("")
    val phoneLiveData = MutableLiveData("")
    val zoneCodeLiveData = MutableLiveData("")
    val addressLiveData = MutableLiveData("")
    val detailAddressLiveData = MutableLiveData("")

    val trySignUpResult = MutableLiveData(-1)

    fun setAddress(zonCode: String, address: String) {
        zoneCodeLiveData.value = zonCode
        addressLiveData.value = address
    }

    fun trySignUp() = onUiWork {
        val userInfo = UserInfo(
            email = checkEmail() ?: return@onUiWork,
            password = checkPassword() ?: return@onUiWork,
            name = checkName() ?: return@onUiWork,
            nickname = checkNickName() ?: return@onUiWork,
            phone = checkPhone() ?: return@onUiWork,
            zoneCode = checkZoneCode() ?: return@onUiWork,
            address = checkAddress() ?: return@onUiWork
        )
    }

    private fun checkEmail(): String? {
        val email = emailLiveData.value.orEmpty()
        return when {
            email.isEmpty() -> trySignUpResult.value = 5
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> trySignUpResult.value = 1
            else -> return email
        }.let { null }
    }

    private fun checkPassword(): String? {
        val password = passwordLiveData.value.orEmpty()
        return when {
            password.isEmpty() -> trySignUpResult.value = 5
            password != passwordCheckLiveData.value -> trySignUpResult.value = 2
            else -> return password
        }.let { null }
    }

    private fun checkName(): String? {
        val name = nameLiveData.value.orEmpty()
        return when {
            name.isEmpty() -> trySignUpResult.value = 5
            else -> return name
        }.let { null }
    }

    private fun checkNickName(): String? {
        val nickName = nicknameLiveData.value.orEmpty()
        return when {
            nickName.isEmpty() -> trySignUpResult.value = 5
            else -> return nickName
        }.let { null }
    }

    private fun checkPhone(): Int? {
        val phone = phoneLiveData.value.orEmpty()
        return when {
            phone.isEmpty() -> trySignUpResult.value = 5
            else -> return phone.toIntOrNull()
        }.let { null }
    }

    private fun checkZoneCode(): Int? {
        val zonCode = zoneCodeLiveData.value.orEmpty()
        return when {
            zonCode.isEmpty() -> trySignUpResult.value = 5
            else -> return zonCode.toIntOrNull()
        }.let { null }
    }

    private fun checkAddress(): String? {
        val address = addressLiveData.value.orEmpty()
        val detailAddress = detailAddressLiveData.value.orEmpty()
        return when {
            address.isEmpty() -> trySignUpResult.value = 5
            detailAddress.isEmpty() -> trySignUpResult.value = 5
            else -> return "$address , $detailAddress"
        }.let { null }
    }
}