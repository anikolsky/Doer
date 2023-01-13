package com.omtorney.doer.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.omtorney.doer.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeNoteAndReadInList() = runBlocking {
        val note = Note(id = 1, noteText = "test note 1")

        noteDao.insert(note)
        val notes = noteDao.getAll().first()

        assertThat(notes.first()).isEqualTo(note)
    }

    @Test
    @Throws(Exception::class)
    fun updateNoteAndReadInList() = runBlocking {
        val note = Note(id = 1, noteText = "test note 1")
        val newText = "test note 2"

        noteDao.insert(note)
        val notes = noteDao.getAll().first()
        noteDao.update(notes.first().copy(noteText = newText))
        val newNotes = noteDao.getAll().first()

        assertThat(newNotes.first().noteText).isEqualTo(newText)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNoteFromList() = runBlocking {
        val note = Note(id = 1, noteText = "test note 1")

        noteDao.insert(note)
        val notes = noteDao.getAll().first()
        noteDao.delete(notes.first())
        val newNotes = noteDao.getAll().first()

        assertThat(newNotes).isEmpty()
    }
}