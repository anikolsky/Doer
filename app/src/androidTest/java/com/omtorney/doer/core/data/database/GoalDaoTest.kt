package com.omtorney.doer.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.goals.domain.model.Step
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class GoalDaoTest {

    private lateinit var goalDao: GoalDao
    private lateinit var db: AppDatabase

//    private lateinit var date: LocalDateTime
    private lateinit var goal: Goal
    private lateinit var step: Step

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        goalDao = db.goalDao()
    }

    @Before
    fun setUp() {
        step = Step(1, "test step 1", false)
        goal = Goal(1, "test goal 1", mutableListOf(), 0L, 0L)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @After
    fun tearDown() {
        goal.steps.clear()
    }

    @Test
    fun addGoal_And_ReadInList() = runTest {
        goalDao.insert(goal)
        val goals = goalDao.getGoals().first()

        assertThat(goals).contains(goal)
    }

    @Test
    fun addGoal_And_ReadInListById() = runTest {
        goalDao.insert(goal)
        val goals = goalDao.getGoals().first()

        assertThat(goals.find { it.id == 1L }).isEqualTo(goal)
    }

    @Test
    fun updateGoal_And_ReadInList() = runTest {
        val newTitle = "test note 2"

        goalDao.insert(goal)
        val goals = goalDao.getGoals().first()
        goalDao.insert(goals.first().copy(title = newTitle))
        val newGoals = goalDao.getGoals().first()

        assertThat(newGoals.first().title).isEqualTo(newTitle)
    }

    @Test
    fun deleteGoalFromList() = runTest {
        goalDao.insert(goal)
        goalDao.delete(goal)

        val newGoals = goalDao.getGoals().first()

        assertThat(newGoals).doesNotContain(goal)
    }

    @Test
    fun addStep_And_ReadInGoal() = runTest {
        goal.steps.add(step)
        goalDao.insert(goal)

        val goals = goalDao.getGoals().first()

        assertThat(goals.first().steps.first()).isEqualTo(step)
    }

    @Test
    fun updateStep_And_ReadInGoal() = runTest {
        val newStepText = "test step 2"

        goal.steps.add(step)
        goalDao.insert(goal)

        val goals = goalDao.getGoals().first()
        val updatedSteps = goals.first().steps.apply {
            find { it.id == 1 }?.text = newStepText
        }
        val updatedGoal = goals.first().copy(steps = updatedSteps)
        goalDao.insert(updatedGoal)
        val updatedGoals = goalDao.getGoals().first()

        assertThat(updatedGoals.first().steps.first().text).isEqualTo(newStepText)
    }

    @Test
    fun deleteStepFromGoal() = runTest {
        goal.steps.add(step)
        goalDao.insert(goal)

        val goals = goalDao.getGoals().first()
        val updatedSteps = goals.first().steps.apply {
            removeIf { it.id == 1 }
        }
        val updatedGoal = goals.first().copy(steps = updatedSteps)
        goalDao.insert(updatedGoal)

        assertThat(goals.first().steps).doesNotContain(step)
    }
}