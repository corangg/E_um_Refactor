package com.data.datasource.remote

import com.data.datasource.RemoteNaverMapDataSource
import javax.inject.Inject

class NaverMapRemoteDataSource @Inject constructor(
    private val naverMapApi: NaverMapApi
): RemoteNaverMapDataSource {
    override suspend fun getAddressToGeoCode(address: String): RemoteGeoCodeData = naverMapApi.getAddressToGeocode(address)
    override suspend fun getReverseGeoCode(coords: String): RemoteReverseGeoCodeData = naverMapApi.getReverseGeocode(coords)
}