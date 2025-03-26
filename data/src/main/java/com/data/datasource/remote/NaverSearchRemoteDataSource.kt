package com.data.datasource.remote

import com.data.datasource.RemoteNaverSearchDataSource
import javax.inject.Inject

class NaverSearchRemoteDataSource @Inject constructor(
    private val naverSearchApi: NaverSearchApi
) : RemoteNaverSearchDataSource {
    override suspend fun getInfoToKeyword(keyword: String): RemoteSearchData = naverSearchApi.getInfoToCoords(keyword)
}