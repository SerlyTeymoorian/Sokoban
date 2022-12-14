package com.bignerdranch.android.sokoban.db

import androidx.room.*
import com.bignerdranch.android.sokoban.logic.HighScore

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM highScore WHERE levelHash = :levelHash AND levelNumber = :levelNumber")
    suspend fun getHighScore(levelHash: Int, levelNumber: Int): List<HighScore>

    suspend fun highScore(levelHash: Int, levelNumber: Int) =
        getHighScore(levelHash, levelNumber).firstOrNull()

    @Update
    suspend fun updateHighScore(highScore: HighScore)

    @Insert
    suspend fun insertHighScore(highScore: HighScore)

    private suspend fun updateHighScoreIfPossible(highScore: HighScore): Boolean {
        val oldScore = highScore(highScore.levelHash, highScore.levelNumber)
        if (oldScore != null) {
            oldScore.improve(highScore)
            updateHighScore(highScore)
            return true
        }
        return false
    }

    @Transaction
    suspend fun addHighScore(highScore: HighScore) {
        if (updateHighScoreIfPossible(highScore)) return
        insertHighScore(highScore)
    }
}
