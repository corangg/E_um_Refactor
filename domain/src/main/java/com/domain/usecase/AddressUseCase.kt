package com.domain.usecase

import com.domain.model.AddressItemData
import com.domain.repository.Repository
import javax.inject.Inject

class UpsertAddressDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(data: AddressItemData) = repository.upsertAddressList(data)
}

class GetAddressListDataUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getAddressListFlow()
}

class DeleteAddressUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(address: String) = repository.deleteAddress(address)
}

class DeleteAllAddressUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.deleteAllAddress()
}

class AddAddressDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(data: AddressItemData): Int {
        val checkResult = repository.checkAddressData(data)
        if (checkResult == 3) {
            repository.upsertAddressList(data)
        }
        return checkResult
    }
}

class GetChoiceAddressDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(position: Int): AddressItemData {
        val list = repository.getAddressList()
        return list[position]
    }
}

class UpdateMainAddressUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(position: Int){
        val addressItemDataList = repository.getAddressList().toMutableList()
        val index = addressItemDataList.indexOfFirst { it.mainValue }
        if(index != -1){
            addressItemDataList[index] = addressItemDataList[index].copy(mainValue = false)
            addressItemDataList[position] = addressItemDataList[position].copy(mainValue = true)
        }
        repository.updateAddressDataList(addressItemDataList)
    }
}