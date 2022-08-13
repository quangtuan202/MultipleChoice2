package com.example.multiplechoice2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class Question(
    // Column info is optional, if this is omitted, the column name will be the same as variable name
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "option_1")
    val option_1: String,
    @ColumnInfo(name = "option_2")
    val option_2: String,
    @ColumnInfo(name = "option_3")
    val option_3: String,
    @ColumnInfo(name = "option_4")
    val option_4: String?,
    @ColumnInfo(name = "answer")
    val answer: Int,
    @ColumnInfo(name = "explanation")
    val explanation: String,
    @Ignore
    var userAnswer: Int
)