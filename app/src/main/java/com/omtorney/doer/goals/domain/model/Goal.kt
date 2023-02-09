package com.omtorney.doer.goals.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "goals")
data class Goal(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val title: String,

    val steps: List<Step>,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,
)

data class Step(
    var id: Int? = null,
    var text: String = ""
)

class GoalStepsConverter {
    @TypeConverter
    fun fromListToJson(steps: List<Step>): String {
        return Gson().toJson(steps)
    }

    @TypeConverter
    fun fromJsonToList(json: String): List<Step> {
        return Gson().fromJson(json, object : TypeToken<List<Step>>() {}.type)
    }
}

class InvalidGoalException(message: String) : Exception(message)