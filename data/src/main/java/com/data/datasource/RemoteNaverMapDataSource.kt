package com.data.datasource

import com.data.datasource.remote.RemoteGeoCodeData
import com.data.datasource.remote.RemoteReverseGeoCodeData

interface RemoteNaverMapDataSource {
    suspend fun getAddressToGeoCode(address: String): RemoteGeoCodeData
    suspend fun getReverseGeoCode(coords: String): RemoteReverseGeoCodeData
}