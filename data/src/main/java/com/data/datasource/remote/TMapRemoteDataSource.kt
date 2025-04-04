package com.data.datasource.remote

import com.data.datasource.RemoteTMapDataSource
import javax.inject.Inject

class TMapRemoteDataSource @Inject constructor(
    private val tMapPublicTransportApi: TMapPublicTransportApi,
    private val tMapCarApi: TMapCarApi,
    private val tMapWalkApi: TMapWalkApi
) : RemoteTMapDataSource {
    override suspend fun getPublicTransportTime(requestData: RemotePublicTransportRouteRequest) =
        tMapPublicTransportApi.getPublicTransportRoute(requestData)

    override suspend fun getCarTime(requestData: RemoteCarRouteRequest) =
        tMapCarApi.getCarRoute(request = requestData)

    override suspend fun getWalkTime(requestData: RemoteWalkRouteRequest) =
        tMapWalkApi.getWalkRoute(request = requestData)
}