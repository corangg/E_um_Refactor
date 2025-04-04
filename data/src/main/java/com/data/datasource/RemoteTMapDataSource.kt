package com.data.datasource

import com.data.datasource.remote.RemoteCarRouteRequest
import com.data.datasource.remote.RemoteCarRouteResponse
import com.data.datasource.remote.RemotePublicTransportRouteResponse
import com.data.datasource.remote.RemotePublicTransportRouteRequest
import com.data.datasource.remote.RemoteWalkRouteRequest
import com.data.datasource.remote.RemoteWalkRouteResponse

interface RemoteTMapDataSource {
    suspend fun getPublicTransportTime(requestData: RemotePublicTransportRouteRequest): RemotePublicTransportRouteResponse

    suspend fun getCarTime(requestData: RemoteCarRouteRequest): RemoteCarRouteResponse?

    suspend fun getWalkTime(requestData: RemoteWalkRouteRequest): RemoteWalkRouteResponse
}