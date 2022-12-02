package com.bignerdranch.android.sokoban.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.sokoban.logic.HighScore
import com.bignerdranch.android.sokoban.GameState

@Database(entities = [(GameState::class), (HighScore::class)], version = 1)
abstract class DatabaseSchema : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
    abstract fun highScoreDao(): HighScoreDao
}
