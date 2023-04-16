package com.omtorney.doer.notes.domain.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omtorney.doer.notes.util.NotePriority
import java.util.*

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @TypeConverters(NoteConverters::class)
    val text: List<String>,

    @TypeConverters(NoteConverters::class)
    val priority: Int,

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
    fun textFromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun textToList(text: String): List<String> {
        return Gson().fromJson(text, object : TypeToken<MutableList<String>>() {}.type)
    }
}

class InvalidNoteException(message: String) : Exception(message)
