package com.bignerdranch.android.sokoban.controls

interface MovementListener {
    fun onMoveLeft()
    fun onMoveRight()
    fun onMoveUp()
    fun onMoveDown()
    fun onResetLevel()
    fun onUndoMove()
}
