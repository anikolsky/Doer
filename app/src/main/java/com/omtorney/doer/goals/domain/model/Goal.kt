package com.omtorney.doer.goals.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "goals")
data class Goal(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val title: String,

    val steps: Steps,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,
)

data class Steps(
    val items: List<String>
)

class GoalStepsConverter {
    @TypeConverter
    fun fromListToJson(steps: Steps): String {
        return Gson().toJson(steps)
    }

    @TypeConverter
    fun fromJsonToList(json: String): Steps {
        return Gson().fromJson(json, Steps::class.java)
    }
}

class InvalidGoalException(message: String) : Exception(message)