package com.bignerdranch.android.sokoban.level

import com.bignerdranch.android.sokoban.logic.Pos
import com.bignerdranch.android.sokoban.logic.Sprite

internal object LevelConverter {
    abstract class LevelConverterException : Exception()

    class NoPlayerTileException : LevelConverterException()

    class ManyPlayerTilesException : LevelConverterException()

    class UnknownTileException : LevelConverterException()

    @Throws(LevelConverterException::class)
    fun convert(data: CharArray, width: Int): ImmutableLevel {
        val player = findPlayer(data, width)
        convertTiles(data)
        return ImmutableLevel(data, width, player)
    }

    @Throws(LevelConverterException::class)
    private fun convertTiles(data: CharArray) {
        for (i in data.indices) {
            data[i] = tileMapping(data[i])
        }
    }

    object CommonLevelFormat {
        const val wall = 'w'
        const val player = '@'
        const val playerOnPoint = '+'
        const val crate = 'b'
        const val boxOnPoint = '!'
        const val point = 'p'
        const val floor = 'g'
    }

    private fun tileWithoutPlayer(tile: Char) = when (tile) {
        CommonLevelFormat.player -> CommonLevelFormat.floor
        CommonLevelFormat.playerOnPoint -> CommonLevelFormat.point
        else -> tile
    }

    @Throws(LevelConverterException::class)
    private fun tileMapping(tile: Char) = when (tile) {
        CommonLevelFormat.crate -> Sprite.box
        CommonLevelFormat.boxOnPoint -> Sprite.boxOnPoint
        CommonLevelFormat.point -> Sprite.endpoint
        CommonLevelFormat.floor -> Sprite.ground
        CommonLevelFormat.wall -> Sprite.wall
        else -> throw UnknownTileException()
    }

    private fun isPlayerTile(tile: Char) = tileWithoutPlayer(tile) != tile

    @Throws(NoPlayerTileException::class, ManyPlayerTilesException::class)
    private fun findPlayer(data: CharArray, width: Int): Pos {
        var result: Pos? = null
        for (i in data.indices) {
            if (isPlayerTile(data[i])) {
                if (result != null) {
                    throw ManyPlayerTilesException()
                }
                data[i] = tileWithoutPlayer(data[i])
                result = Pos(i / width, i % width)
            }
        }
        if (result == null) {
            throw NoPlayerTileException()
        }
        return result
    }
}
