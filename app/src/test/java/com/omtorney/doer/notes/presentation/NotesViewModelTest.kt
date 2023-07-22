package com.omtorney.doer.notes.presentation

import androidx.lifecycle.SavedStateHandle
import com.omtorney.doer.data.RepositoryImpl
import com.omtorney.doer.data.local.datastore.SettingsStore
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.domain.usecases.NoteUseCases
import com.omtorney.doer.presentation.screen.notes.noteedit.NoteEditEvent
import com.omtorney.doer.presentation.screen.notes.noteedit.NoteEditViewModel
import com.omtorney.doer.presentation.screen.notes.notelist.NotesViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var note: Note
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var noteEditViewModel: NoteEditViewModel
    private lateinit var noteUseCases: NoteUseCases
    private lateinit var settingsDataStore: SettingsStore
    private lateinit var savedStateHandle: SavedStateHandle
    @MockK lateinit var repository: RepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        note = Note(1, "test note 1", "note content 1", 1, "ToDo", 0L, 0L)
        savedStateHandle = SavedStateHandle()
        notesViewModel = NotesViewModel(noteUseCases)
        noteEditViewModel = NoteEditViewModel(noteUseCases, savedStateHandle)

//        coEvery {
//            noteUseCases.addNote(note)
//        } returns
    }

    @After
    fun close() {
        Dispatchers.resetMain()
    }

    @Test
    fun deleteNote() = runTest {
        noteEditViewModel.onEvent(NoteEditEvent.Save)

        coEvery {  }
    }
}
