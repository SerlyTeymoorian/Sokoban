package com.bignerdranch.android.sokoban.logic

data class Pos (val y: Int = 0, val x: Int = 0) {
    //first index is the y position and the second index is the x position 
    infix operator fun plus(other: Pos) = Pos(y + other.y, x + other.x)
}
