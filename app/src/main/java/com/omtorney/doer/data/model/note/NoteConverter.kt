package com.omtorney.doer.data.model.note

import androidx.room.TypeConverter

class NoteConverter {
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
