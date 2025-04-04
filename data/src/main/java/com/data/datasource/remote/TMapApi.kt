package com.data.datasource.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TMapPublicTransportApi {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("transit/routes")
    suspend fun getPublicTransportRoute(
        @Body request: RemotePublicTransportRouteRequest
    ): RemotePublicTransportRouteResponse
}

interface TMapCarApi {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("tmap/routes")
    suspend fun getCarRoute(
        @Query("version") version: String = "1",
        @Body request: RemoteCarRouteRequest
    ): RemoteCarRouteResponse
}

interface TMapWalkApi {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("tmap/routes/pedestrian")
    suspend fun getWalkRoute(
        @Query("version") version: String = "1",
        @Body request: RemoteWalkRouteRequest
    ): RemoteWalkRouteResponse
}