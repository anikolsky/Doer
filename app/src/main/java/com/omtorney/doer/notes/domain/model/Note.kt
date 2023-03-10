package com.omtorney.doer.notes.domain.model

import androidx.room.*
import com.omtorney.doer.notes.util.NotePriority
import java.util.*

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val text: String,

    @TypeConverters(NotePriorityConverter::class)
    val priority: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,

    @ColumnInfo(name = "pinned")
    var isPinned: Boolean = false
)

class NotePriorityConverter {
    @TypeConverter
    fun fromInt(value: Int): NotePriority {
        return when (value) {
            1 -> NotePriority.High
            2 -> NotePriority.Medium
            3 -> NotePriority.Low
            4 -> NotePriority.No
            else -> throw IllegalArgumentException("Invalid priority")
        }
    }

    @TypeConverter
    fun toInt(priority: NotePriority): Int {
        return priority.index
    }
}

class InvalidNoteException(message: String) : Exception(message)
