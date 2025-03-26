package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.PlaceItem
import com.domain.usecase.GetAddressToCoordinateUseCase
import com.domain.usecase.GetCoordinateToAddressUseCase
import com.domain.usecase.GetSearchKeywordList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MapActivityViewModel @Inject constructor(
    private val getCoordinateToAddressUseCase: GetCoordinateToAddressUseCase,
    private val getAddressToCoordinateUseCase: GetAddressToCoordinateUseCase,
    private val getSearchKeywordList: GetSearchKeywordList,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val coordinateLiveData = MutableLiveData(Pair(0.0, 0.0))
    val textSearchKeyword = MutableLiveData("")
    val getSelectPlace = MutableLiveData<PlaceItem>()
    val getSearchPlaceList = MutableLiveData<List<PlaceItem>>(listOf())

    fun getCoordinateToAddress(address: String) = onUiWork {
        coordinateLiveData.value = getCoordinateToAddressUseCase(address)
    }

    fun getAddressToCoordinate(
        latitude: String,
        longitude: String,
        currentLocation: Pair<Double, Double>
    ) = onUiWork {
        getSelectPlace.value = getAddressToCoordinateUseCase(longitude, latitude, currentLocation) ?: return@onUiWork
    }

    fun searchKeyword(location: Pair<Double, Double>) = onUiWork {
        val keyword = textSearchKeyword.value ?: return@onUiWork
        getSearchPlaceList.value = getSearchKeywordList(keyword, location)
    }
}