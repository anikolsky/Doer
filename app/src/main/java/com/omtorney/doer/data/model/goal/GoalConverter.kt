package com.omtorney.doer.data.model.goal

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GoalConverter {
    @TypeConverter
    fun fromListToJson(steps: MutableList<Step>): String {
        return Gson().toJson(steps)
    }

    @TypeConverter
    fun fromJsonToList(json: String): MutableList<Step> {
        return Gson().fromJson(json, object : TypeToken<MutableList<Step>>() {}.type)
    }
}
