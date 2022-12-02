package com.bignerdranch.android.sokoban.level

import com.bignerdranch.android.sokoban.logic.Pos
import com.bignerdranch.android.sokoban.logic.Sprite
import com.bignerdranch.android.sokoban.Textures

abstract class LevelAbstract {
    abstract val sprites: CharArray
    abstract val width: Int
    abstract val playerPos: Pos

    fun tileIndex(pos: Pos) = pos.y * width + pos.x

    fun tile(pos: Pos) = sprites[tileIndex(pos)]
    fun texture(pos: Pos) = Textures.tile(tile(pos))

    override fun toString(): String {
        var result = "" + height() + ' ' + width + '\n' +
                playerPos.y + ' ' + playerPos.x + '\n'
        for (i in 0 until height()) {
            for (j in 0 until width) {
                result += tile(Pos(i, j))
            }
            result += "\n"
        }
        return result
    }

    private fun count(tileType: Char) = sprites.count { it == tileType }
    private fun countCrates() = count(Sprite.box)
    private fun countEndpoints() = count(Sprite.endpoint)

    fun won() = countCrates() == 0 && countEndpoints() == 0

    private fun validTile(pos: Pos) = pos.x in 0 until width && pos.y in 0 until height()
    protected fun canMove(pos: Pos) = validTile(pos) && Sprite.isWalkable(tile(pos))
    protected fun isCrate(pos: Pos) = validTile(pos) && Sprite.isCrate(tile(pos))

    fun height() = sprites.size / width

    override fun equals(other: Any?) = when (other) {
        is ImmutableLevel ->
            width == other.width &&
                    sprites.contentEquals(other.sprites)
        else -> false
    }

    fun positions(): List<Pos> {
        val res = mutableListOf<Pos>()
        for (y in 0 until height()) {
            (0 until width).mapTo(res) { Pos(y, it) }
        }
        return res
    }

    override fun hashCode() = toString().hashCode()
}
