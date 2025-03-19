package com.data.datasource.local.room.converter

import androidx.room.TypeConverter
import com.data.datasource.local.room.LocalChatMessageData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatDataConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromChatList(list: List<LocalChatMessageData>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toChatList(json: String): List<LocalChatMessageData> {
        val type = object : TypeToken<List<LocalChatMessageData>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}