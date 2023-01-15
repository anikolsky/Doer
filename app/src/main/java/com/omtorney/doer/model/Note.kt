package com.omtorney.doer.model

import androidx.compose.ui.graphics.Color
import androidx.room.*
import com.omtorney.doer.util.Constants
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "text")
    val text: String,

    @TypeConverters(NotePriorityConverter::class)
    @ColumnInfo(name = "priority")
    val priority: NotePriority,

    @TypeConverters(LocalDateTimeConverter::class)
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime?,

    @TypeConverters(LocalDateTimeConverter::class)
    @ColumnInfo(name = "changed_at")
    val changedAt: LocalDateTime?
)

sealed class NotePriority(
    val status: String,
    val color: Color
) {
    object High : NotePriority("1", Color(Constants.highPriorityColor))
    object Medium : NotePriority("2", Color(Constants.mediumPriorityColor))
    object Low : NotePriority("3", Color(Constants.lowPriorityColor))
    object No : NotePriority("4", Color.Gray)
}

class NotePriorityConverter {
    @TypeConverter
    fun fromString(value: String): NotePriority {
        return when (value) {
            "1" -> NotePriority.High
            "2" -> NotePriority.Medium
            "3" -> NotePriority.Low
            "4" -> NotePriority.No
            else -> throw IllegalArgumentException("Invalid priority")
        }
    }

    @TypeConverter
    fun notePriorityToString(priority: NotePriority): String {
        return priority.status
    }
}

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime): Long {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}

