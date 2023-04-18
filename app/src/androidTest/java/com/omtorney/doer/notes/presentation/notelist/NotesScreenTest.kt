package com.omtorney.doer.notes.presentation.notelist

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import com.omtorney.doer.core.presentation.DoerApp
import com.omtorney.doer.core.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
//        composeTestRule.activity.setContent { DoerApp() }
    }

//    @After
//    fun close() {
//
//    }

    @Test
    fun assert_IsLazyColumnDisplayed() {
        composeTestRule.onNodeWithTag("NOTES_SCREEN_LAZY_COLUMN").assertIsDisplayed()
    }

    @Test
    fun addNote_Assert_IsNoteDisplayed() {
        composeTestRule.onNodeWithContentDescription("Add").performClick() // FIXME
        composeTestRule.onNodeWithTag("NOTE_EDIT_TEXT_FIELD").performTextInput("Test note")
        composeTestRule.onNodeWithContentDescription("Save").performClick()

        composeTestRule.onAllNodesWithTag("NOTE_ITEM").assertAny(hasText("Test note"))
    }
}