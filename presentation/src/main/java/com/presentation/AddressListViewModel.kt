package com.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.DeleteAddressUseCase
import com.domain.usecase.GetAddressListDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    getAddressListDataUseCase: GetAddressListDataUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val addressListData = getAddressListDataUseCase().asLiveData(viewModelScope.coroutineContext)

    val messageType = MutableLiveData(-1)


    fun deleteAddress(position: Int) = onUiWork {
        val addressList = addressListData.value
        if (addressList != null && addressList.size > 1) {
            deleteAddressUseCase(addressList[position].address)
        } else {
            messageType.value = 1
        }
    }
}