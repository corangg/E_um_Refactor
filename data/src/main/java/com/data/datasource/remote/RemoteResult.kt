package com.data.datasource.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoteGeoCodeData(
    @Expose @SerializedName("addresses") val addresses: List<RemoteAddressData>,
    @Expose @SerializedName("errorMessage") val errorMessage: String,
    @Expose @SerializedName("meta") val meta: RemoteMetaData,
    @Expose @SerializedName("status") val status: String
)

data class RemoteAddressData(
    @Expose @SerializedName("addressElements") val addressElements: List<RemoteAddressElementData>,
    @Expose @SerializedName("distance") val distance: Double,
    @Expose @SerializedName("englishAddress") val englishAddress: String,
    @Expose @SerializedName("jibunAddress") val jibunAddress: String,
    @Expose @SerializedName("roadAddress") val roadAddress: String,
    @Expose @SerializedName("x") val x: String,
    @Expose @SerializedName("y") val y: String
)

data class RemoteMetaData(
    @Expose @SerializedName("count") val count: Int,
    @Expose @SerializedName("page") val page: Int,
    @Expose @SerializedName("totalCount") val totalCount: Int
)

data class RemoteAddressElementData(
    @Expose @SerializedName("code") val code: String,
    @Expose @SerializedName("longName") val longName: String,
    @Expose @SerializedName("shortName") val shortName: String,
    @Expose @SerializedName("types") val types: List<String>
)

data class RemoteReverseGeoCodeData(
    @Expose @SerializedName("results") val results: List<RemoteResult>,
    @Expose @SerializedName("status") val status: RemoteStatus
)

data class RemoteResult(
    @Expose @SerializedName("code") val code: RemoteCode,
    @Expose @SerializedName("land") val land: RemoteLand,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("region") val region: RemoteRegion
)

data class RemoteCode(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("mappingId") val mappingId: String?,
    @Expose @SerializedName("type") val type: String?
)

data class RemoteLand(
    @Expose @SerializedName("addition0") val addition0: RemoteAddition,
    @Expose @SerializedName("addition1") val addition1: RemoteAddition,
    @Expose @SerializedName("addition2") val addition2: RemoteAddition,
    @Expose @SerializedName("addition3") val addition3: RemoteAddition,
    @Expose @SerializedName("addition4") val addition4: RemoteAddition,
    @Expose @SerializedName("coords") val coords: RemoteCoords,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("number1") val number1: String?,
    @Expose @SerializedName("number2") val number2: String?,
    @Expose @SerializedName("type") val type: String?
)

data class RemoteAddition(
    @Expose @SerializedName("type") val type: String?,
    @Expose @SerializedName("value") val value: String?
)

data class RemoteCoords(
    @Expose @SerializedName("center") val center: RemoteCenter
)

data class RemoteCenter(
    @Expose @SerializedName("crs") val crs: String?,
    @Expose @SerializedName("x") val x: Double,
    @Expose @SerializedName("y") val y: Double
)

data class RemoteRegion(
    @Expose @SerializedName("area0") val area0: RemoteArea0,
    @Expose @SerializedName("area1") val area1: RemoteArea1,
    @Expose @SerializedName("area2") val area2: RemoteArea0,
    @Expose @SerializedName("area3") val area3: RemoteArea0,
    @Expose @SerializedName("area4") val area4: RemoteArea0
)

data class RemoteArea0(
    @Expose @SerializedName("coords") val coords: RemoteCoords,
    @Expose @SerializedName("name") val name: String?
)

data class RemoteArea1(
    @Expose @SerializedName("alias") val alias: String?,
    @Expose @SerializedName("coords") val coords: RemoteCoords,
    @Expose @SerializedName("name") val name: String?
)

data class RemoteStatus(
    @Expose @SerializedName("code") val code: Int,
    @Expose @SerializedName("message") val message: String?,
    @Expose @SerializedName("name") val name: String?
)

data class RemoteSearchData(
    @Expose @SerializedName("items") val items: List<RemotePlaceItem>
)

data class RemotePlaceItem(
    @Expose @SerializedName("title") val title: String,
    @Expose @SerializedName("link") val link: String,
    @Expose @SerializedName("category") val category: String,
    @Expose @SerializedName("description") val description: String,
    @Expose @SerializedName("address") val address: String,
    @Expose @SerializedName("roadAddress") val roadAddress: String,
    @Expose @SerializedName("mapx") val x: Double,
    @Expose @SerializedName("mapy") val y: Double,
)
