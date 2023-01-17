package com.omtorney.doer.model

import androidx.room.*
import com.omtorney.doer.util.NotePriority
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

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,

    @ColumnInfo(name = "pinned")
    var isPinned: Boolean = false
)

class NotePriorityConverter {
    @TypeConverter
    fun fromByte(value: Int): NotePriority {
        return when (value) {
            1 -> NotePriority.High
            2 -> NotePriority.Medium
            3 -> NotePriority.Low
            4 -> NotePriority.No
            else -> throw IllegalArgumentException("Invalid priority")
        }
    }
    @TypeConverter
    fun notePriorityToString(priority: NotePriority): Int {
        return priority.status
    }
}

class InvalidNoteException(message: String): Exception(message)
