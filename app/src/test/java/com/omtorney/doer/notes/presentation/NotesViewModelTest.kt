package com.omtorney.doer.notes.presentation

import androidx.lifecycle.SavedStateHandle
import com.omtorney.doer.core.data.RepositoryImpl
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.usecase.NoteUseCases
import com.omtorney.doer.notes.presentation.noteedit.NoteEditEvent
import com.omtorney.doer.notes.presentation.noteedit.NoteEditViewModel
import com.omtorney.doer.notes.presentation.notelist.NotesViewModel
import com.omtorney.doer.settings.domain.usecase.SettingsUseCases
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
    private lateinit var settingsUseCases: SettingsUseCases
    private lateinit var savedStateHandle: SavedStateHandle
    @MockK lateinit var repository: RepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        note = Note(1, "test note 1", 1, 0L, 0L)
        savedStateHandle = SavedStateHandle()
        notesViewModel = NotesViewModel(noteUseCases, settingsUseCases)
        noteEditViewModel = NoteEditViewModel(noteUseCases, settingsUseCases, savedStateHandle)

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