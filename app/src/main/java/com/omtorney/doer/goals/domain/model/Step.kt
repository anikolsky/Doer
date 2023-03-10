package com.omtorney.doer.goals.domain.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Step(
    var id: Int? = null,
    var text: String = "",
    var isAchieved: Boolean = false
)

class GoalStepsConverter {
    @TypeConverter
    fun fromListToJson(steps: MutableList<Step>): String {
        return Gson().toJson(steps)
    }

    @TypeConverter
    fun fromJsonToList(json: String): MutableList<Step> {
        return Gson().fromJson(json, object : TypeToken<MutableList<Step>>() {}.type)
    }
}
