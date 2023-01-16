package com.omtorney.doer.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.omtorney.doer.model.Note
import com.omtorney.doer.model.NotePriority
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDatabase
    private lateinit var date: LocalDateTime
    private lateinit var note: Note

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        noteDao = db.noteDao()
    }

    @Before
    fun setUp() {
        date = LocalDateTime.of(2023, Month.JANUARY, 15, 12, 0, 0)
        note = Note(1, "test note 1", NotePriority.High, date, date)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//    @After
//    fun tearDown() {
//        note = null
//    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadInList() = runBlocking {
        val note = note

        noteDao.insert(note)
        val notes = noteDao.getNotes().first()

        assertThat(notes.first()).isEqualTo(note)
    }

    @Test
    @Throws(Exception::class)
    fun updateNoteAndReadInList() = runBlocking {
        val note = note
        val newText = "test note 2"

        noteDao.insert(note)
        val notes = noteDao.getNotes().first()
        noteDao.update(notes.first().copy(text = newText))
        val newNotes = noteDao.getNotes().first()

        assertThat(newNotes.first().text).isEqualTo(newText)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNoteFromList() = runBlocking {
        val note = note

        noteDao.insert(note)
        val notes = noteDao.getNotes().first()
        noteDao.delete(notes.first())
        val newNotes = noteDao.getNotes().first()

        assertThat(newNotes).isEmpty()
    }
}