package com.data.datasource.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NaverMapApi {
    @GET("map-geocode/v2/geocode")
    suspend fun getAddressToGeocode(
        @Query("query") address: String
    ): RemoteGeoCodeData

    @GET("map-reversegeocode/v2/gc")
    suspend fun getReverseGeocode(
        @Query("coords") coords: String,
        @Query("orders") orders: String = "roadaddr,addr",
        @Query("output") output: String = "json"
    ): RemoteReverseGeoCodeData
}