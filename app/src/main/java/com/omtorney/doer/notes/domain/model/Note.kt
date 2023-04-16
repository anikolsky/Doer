package com.omtorney.doer.notes.domain.model

import androidx.room.*
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @TypeConverters(NoteConverters::class)
    val priority: Int,
    @TypeConverters(NoteConverters::class)
    val status: NoteStatus,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,
    @ColumnInfo(name = "pinned")
    var isPinned: Boolean = false
)

class NoteConverters {
    @TypeConverter
    fun priorityFromInt(value: Int): NotePriority {
        return when (value) {
            1 -> NotePriority.High
            2 -> NotePriority.Medium
            3 -> NotePriority.Low
            4 -> NotePriority.No
            else -> throw IllegalArgumentException("Invalid priority")
        }
    }
    @TypeConverter
    fun priorityToInt(priority: NotePriority): Int {
        return priority.index
    }
    @TypeConverter
    fun statusFromString(value: String): NoteStatus {
        return NoteStatus.valueOf(value)
    }
    @TypeConverter
    fun statusToString(noteStatus: NoteStatus): String {
        return noteStatus.name
    }
}

class InvalidNoteException(message: String) : Exception(message)
