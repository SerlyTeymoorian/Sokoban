package com.bignerdranch.android.sokoban.level

import android.content.Context

interface LevelLoader {
    fun load(context: Context, levelNumber: Int): ImmutableLevel
}