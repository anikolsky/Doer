package com.omtorney.doer.data.model.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val priority: NotePriority,
    val status: NoteStatus,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long,
    @ColumnInfo(name = "pinned")
    var isPinned: Boolean = false
)

class InvalidNoteException(message: String) : Exception(message)
