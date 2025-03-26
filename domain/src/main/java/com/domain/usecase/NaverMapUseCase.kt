package com.domain.usecase

import com.core.util.calculateDistance
import com.core.util.changeCoordinate
import com.domain.model.PlaceItem
import com.domain.repository.Repository
import java.util.Locale
import javax.inject.Inject

class GetCoordinateToAddressUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(address: String): Pair<Double, Double> {
        val geoCodeData = repository.getGeoCode(address)
        return Pair(
            geoCodeData.addresses.first().x.toDouble(),
            geoCodeData.addresses.first().y.toDouble()
        )
    }
}

class GetAddressToCoordinateUseCase @Inject constructor(
    private val repository: Repository,
    private val searchKeyword: SearchKeyword
) {
    suspend operator fun invoke(
        x: String,
        y: String,
        currentLocation: Pair<Double, Double>
    ): PlaceItem? {
        val coords = "$x,$y"
        val addressData = repository.getReverseGeoCode(coords).results.first()
        val address = if (addressData.land.number2 != "") {
            "${addressData.region.area1.name} ${addressData.region.area2.name} ${addressData.region.area3.name} ${addressData.land.name} ${addressData.land.number1}-${addressData.land.number2}"
        } else {
            "${addressData.region.area1.name} ${addressData.region.area2.name} ${addressData.region.area3.name} ${addressData.land.name} ${addressData.land.number1}"
        }
        val firstPlace = searchKeyword(address).firstOrNull() ?: return null
        val distance = calculateDistance(
            currentLocation.first,
            currentLocation.second,
            changeCoordinate(firstPlace.y),
            changeCoordinate(firstPlace.x)
        )
        return firstPlace.copy(
            title = firstPlace.title.replace("<b>", "").replace("</b>", ""),
            distance = distance
        )
    }
}

class GetSearchKeywordList @Inject constructor(private val searchKeyword: SearchKeyword) {
    suspend operator fun invoke(keyword: String, location: Pair<Double, Double>): List<PlaceItem> {
        val list = searchKeyword(keyword)
        return list.map { placeItem ->
            val distance = calculateDistance(
                location.first,
                location.second,
                changeCoordinate(placeItem.y),
                changeCoordinate(placeItem.x)
            )
            placeItem.copy(
                title = placeItem.title.replace("<b>", "").replace("</b>", ""),
                distance = distance
            )
        }
    }
}

class SearchKeyword @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(keyword: String) = repository.getKeywordSearch(keyword).items
}