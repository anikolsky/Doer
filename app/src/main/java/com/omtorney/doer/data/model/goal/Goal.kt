package com.omtorney.doer.data.model.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val steps: MutableList<Step>,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long
)

class InvalidGoalException(message: String) : Exception(message)
