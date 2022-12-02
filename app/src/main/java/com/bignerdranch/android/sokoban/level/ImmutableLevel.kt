package com.bignerdranch.android.sokoban.level

import com.bignerdranch.android.sokoban.logic.Pos

class ImmutableLevel(
    override val sprites: CharArray,
    override val width: Int,
    override val playerPos: Pos
) : LevelAbstract()