package com.bignerdranch.android.sokoban

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.sokoban.controls.MovementListener
import com.bignerdranch.android.sokoban.db.Database
import com.bignerdranch.android.sokoban.logic.HighScore
import com.bignerdranch.android.sokoban.logic.Move
import com.bignerdranch.android.sokoban.level.Level
import com.bignerdranch.android.sokoban.level.LevelLoader
import com.bignerdranch.android.sokoban.level.ResourceLevelLoader
import com.bignerdranch.android.sokoban.level.getDefaultLevel
import com.bignerdranch.android.sokoban.GameState.Companion.createGameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class GameModel(private val context: Context, private val levelLoader: LevelLoader = ResourceLevelLoader) : ViewModel(),
    MovementListener {
    val statsLive: MutableLiveData<HighScore> = MutableLiveData()
    val levelNumberLive: MutableLiveData<Int> = MutableLiveData()
    val levelLive: MutableLiveData<Level> = MutableLiveData()
    val movesLive: MutableLiveData<ArrayList<Move>> = MutableLiveData()
    val wonLive: MutableLiveData<Boolean> = MutableLiveData()

    init {
        resetStats()
        levelNumberLive.value = 1
        levelLive.value = getDefaultLevel()
        movesLive.value = ArrayList()
        updateWon()
    }

    private fun stats() = statsLive.value ?: HighScore(levelNumber())
    fun levelNumber() = levelNumberLive.value ?: 0
    fun level() = levelLive.value ?: getDefaultLevel()
    fun player() = level().playerPos
    private fun moves() = movesLive.value ?: ArrayList()

    private fun addMove(move: Move) {
        movesLive.value = ArrayList(moves().plus(move))
    }

    fun lastMove() = moves().lastOrNull()

    @Throws(Move.UnknownMoveException::class)
    fun makeMoves(movesList: String) {
        for (c in movesList.toCharArray()) {
            makeMove(Move.fromChar(c))
        }
    }

    override fun onResetLevel() {
        levelLive.value = try {
            Level(levelLoader.load(context, levelNumber()))
        } catch (e: IOException) {
            getDefaultLevel()
        }
        resetStats()
        movesLive.value = ArrayList()
    }

    private fun resetStats() {
        statsLive.value = HighScore(levelNumber())
    }

    override fun onUndoMove() {
        val toUndo = lastMove()
        toUndo?.let {
            moves().removeAt(moves().lastIndex)
            level().undo(toUndo)
///////////////////////            stats().moves--
            if (toUndo.push) {
                stats().pushes--
            }
            afterMove()
        }
    }

    private fun afterMove() {
        updateWon()
        updateStats()
        updateLevel()
    }

    private fun makeMove(move: Move) {
        val moveMade = level().move(move) ?: return
        addMove(moveMade)
        stats().moves++
        if (moveMade.push) {
            stats().pushes++
        }
        afterMove()
    }

    private fun updateWon() {
        val won = level().won()
        if (wonLive.value != won) {
            wonLive.value = won
        }
    }

    override fun onMoveUp() = makeMove(Move.UP)
    override fun onMoveDown() = makeMove(Move.DOWN)
    override fun onMoveLeft() = makeMove(Move.LEFT)
    override fun onMoveRight() = makeMove(Move.RIGHT)

    private fun updateStats() {
        statsLive.value = stats()
    }

    private fun updateLevel() {
        levelLive.value = level()
    }

    fun nextLevel() {
        levelNumberLive.value = levelNumber() + 1
        onResetLevel()
    }

    fun sendHighScore() {
        stats().levelHash = levelHash()
        stats().levelNumber = levelNumber()
        viewModelScope.launch(Dispatchers.IO) {
            Database.highScoreDao.addHighScore(stats())
        }
    }

    fun onSecondElapsed() {
        stats().time++
        updateStats()
    }

    fun startLevel(levelNumber: Int) {
        levelNumberLive.value = levelNumber
        onResetLevel()
    }

    private fun levelHash() = level().hashCode()

    fun setTime(time: Int) {
        resetStats()
        stats().time = time
    }

    fun gameState() = createGameState(stats().time, levelNumber(), moves())
}

class GameModelViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameModel(context) as T
    }
}