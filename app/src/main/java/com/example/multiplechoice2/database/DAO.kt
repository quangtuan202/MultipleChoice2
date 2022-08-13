package com.example.multiplechoice2.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Insert
    suspend fun insertAll(questions: List<Question>)

    @Query("SELECT * FROM question_table where uid=:question_id")
    suspend fun getQuestionById(question_id:Int): Question

    @Query("SELECT * FROM question_table")
    suspend fun getAllQuestion(): List<Question>

    @Query("DELETE FROM question_table where uid=:question_id")
    suspend fun deleteQuestionById(question_id:Int)

    @Query("DELETE FROM question_table")
    suspend fun deleteAll()

}