package com.omtorney.doer.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.omtorney.doer.data.local.database.AppDatabase
import com.omtorney.doer.data.local.database.NoteDao
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NoteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class NoteEditDaoTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDatabase
//    private lateinit var date: LocalDateTime
    private lateinit var note: Note

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        noteDao = db.noteDao()
    }

    @Before
    fun setUp() {
        note = Note(1, "title", "test note 1", 1, NoteStatus.ToDo,0L, 0L)
    }

    @After
    fun closeDb() {
        db.close()
    }

//    @After
//    fun tearDown() {
//        note = null
//    }

    @Test
    fun addNote_And_ReadInList() = runTest {
        noteDao.insert(note)
        val notes = noteDao.getNotes().first()

        assertThat(notes).contains(note)
    }

    @Test
    fun addNote_And_ReadInListById() = runTest {
        noteDao.insert(note)
        val notes = noteDao.getNotes().first()

        assertThat(notes.find { it.id == 1L }).isEqualTo(note)
    }

    @Test
    fun updateNote_And_ReadInList() = runTest {
        val newText = "test note 2"

        noteDao.insert(note)
        val notes = noteDao.getNotes().first()
        noteDao.insert(notes.first().copy(content = newText))
        val newNotes = noteDao.getNotes().first()

        assertThat(newNotes.first().content).isEqualTo(newText)
    }

    @Test
    fun deleteNoteFromList() = runTest {
        noteDao.insert(note)
        noteDao.delete(note)
        val newNotes = noteDao.getNotes().first()

        assertThat(newNotes).doesNotContain(note)
    }
}