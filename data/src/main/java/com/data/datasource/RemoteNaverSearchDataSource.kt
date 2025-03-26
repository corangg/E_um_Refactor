package com.data.datasource

import com.data.datasource.remote.RemoteSearchData

interface RemoteNaverSearchDataSource {
    suspend fun getInfoToKeyword(keyword: String): RemoteSearchData
}