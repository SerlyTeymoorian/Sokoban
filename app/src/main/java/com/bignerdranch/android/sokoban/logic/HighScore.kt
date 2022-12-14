package com.bignerdranch.android.sokoban.logic

import androidx.room.Entity
import kotlin.math.min

@Entity(primaryKeys = [("levelNumber"), ("levelHash")])
data class HighScore(
    var levelNumber: Int,
    var time: Int = 0,
    var moves: Int = 0,
    var pushes: Int = 0,
    var levelHash: Int = 0
) {
    fun improve(another: HighScore) {
        time = min(time, another.time)
        moves = min(moves, another.moves)
        pushes = min(pushes, another.pushes)
    }

    override fun equals(other: Any?) = when (other) {
        is HighScore ->
            time == other.time &&
                    moves == other.moves &&
                    pushes == other.pushes
        else -> false
    }

    override fun toString() = "HighScore(" +
            "time=" + time + ", " +
            "movesLive=" + moves + ", " +
            "pushes=" + pushes + ")"

    override fun hashCode() = toString().hashCode()
}

