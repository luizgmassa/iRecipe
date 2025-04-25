package com.massa.irecipe.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromBaseIngredientsList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBaseIngredientsList(value: String): List<String> {
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }
}