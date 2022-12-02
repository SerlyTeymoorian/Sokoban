package com.bignerdranch.android.sokoban.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.bignerdranch.android.sokoban.GameState

@Dao
interface GameStateDao {
    @Query("SELECT * FROM gameState")
    suspend fun getGameState(): List<GameState>

    @Insert
    suspend fun insertGameState(gameState: GameState)

    @Query("DELETE FROM gameState")
    suspend fun deleteAllGameState()

    @Transaction
    suspend fun setCurrentGame(gameState: GameState) {
        deleteAllGameState()
        insertGameState(gameState)
    }

    suspend fun getCurrentGame() = getGameState().firstOrNull()
}
