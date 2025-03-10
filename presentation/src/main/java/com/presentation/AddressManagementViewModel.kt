package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.AddressItemData
import com.domain.usecase.AddAddressDataUseCase
import com.domain.usecase.GetChoiceAddressDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AddressManagementViewModel @Inject constructor(
    private val addAddressDataUseCase: AddAddressDataUseCase,
    private val getChoiceAddressDataUseCase: GetChoiceAddressDataUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val zoneCodeLiveData = MutableLiveData("")
    val addressLiveData = MutableLiveData("")
    val detailAddressLiveData = MutableLiveData("")
    val addressNameLiveData = MutableLiveData("")

    val messageType = MutableLiveData(-1)

    fun getChoiceAddressData(position: Int) = onUiWork {
        val addressData = getChoiceAddressDataUseCase(position)
        val splitAddress = addressData.address.split(",")
        zoneCodeLiveData.value = addressData.zoneCode
        addressLiveData.value = splitAddress.firstOrNull() ?: ""
        detailAddressLiveData.value = splitAddress.drop(1).joinToString(",")
        addressNameLiveData.value = addressData.title
    }

    fun setAddress(zoneCode: String, address: String) = onUiWork {
        zoneCodeLiveData.value = zoneCode
        addressLiveData.value = address
    }

    fun saveAddress() = onUiWork {
        val data = AddressItemData(
            title = checkTitle() ?: return@onUiWork,
            address = checkAddress() ?: return@onUiWork,
            zoneCode = zoneCodeLiveData.value ?: "",
            mainValue = false
        )
        messageType.value = addAddressDataUseCase(data)
    }

    private fun checkTitle(): String? {
        return if (addressNameLiveData.value != "") {
            addressNameLiveData.value
        } else {
            messageType.value = 5
            null
        }
    }

    private fun checkAddress(): String? {
        val address = addressLiveData.value.orEmpty()
        val detailAddress = detailAddressLiveData.value.orEmpty()
        return when {
            address.isEmpty() -> messageType.value = 4
            detailAddress.isEmpty() -> messageType.value = 4
            else -> return "$address , $detailAddress"
        }.let { null }
    }
}