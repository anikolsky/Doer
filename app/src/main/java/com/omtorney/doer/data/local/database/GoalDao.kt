package com.omtorney.doer.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.omtorney.doer.data.model.goal.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goal>>

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: Long): Goal?

    @Upsert
    suspend fun upsert(goal: Goal)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(goals: List<GoalEdit>)

    @Delete
    suspend fun delete(goal: Goal)
}
