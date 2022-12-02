package com.bignerdranch.android.sokoban.level

import com.bignerdranch.android.sokoban.logic.Move
import com.bignerdranch.android.sokoban.logic.Pos
import com.bignerdranch.android.sokoban.logic.Sprite

class Level(private val original: ImmutableLevel) : LevelAbstract() {
    override val sprites: CharArray = original.sprites.copyOf()
    override var playerPos: Pos = original.playerPos.copy()
    override val width: Int = original.width

    private fun setSprite(pos: Pos, c: Char) {
        sprites[tileIndex(pos)] = c
    }

    private fun push(from: Pos, to: Pos) {
        val oldTile = tile(from)
        val newTile = tile(to)
        setSprite(to, Sprite.withCrate(newTile))
        setSprite(from, Sprite.withoutCrate(oldTile))
    }

    fun move(move: Move): Move? {
        val delta = move.toPosition()
        val movePos = playerPos + delta
        val pushPos = movePos + delta
        if (isCrate(movePos) && canMove(pushPos)) {
            push(movePos, pushPos)
            playerPos = movePos
            return Move.makePush(move)
        } else if (canMove(movePos)) {
            playerPos = movePos
            return move
        }
        return null
    }

    fun undo(move: Move) {
        val delta = move.toPosition()
        if (move.push && isCrate(playerPos + delta)) {
            push(playerPos + delta, playerPos)
        }
        playerPos += move.reverse().toPosition()
    }

    override fun hashCode() = original.hashCode()

    override fun equals(other: Any?) = when (other) {
        is Level ->
            sprites.contentEquals(other.sprites) &&
                    playerPos == other.playerPos &&
                    width == other.width
        else -> false
    }
}
fun getDefaultImmutableLevel() = ImmutableLevel(
    sprites = ("bbbbb" + "bggbb" + "bbbbb").toCharArray(),
    width = 5,
    playerPos = Pos(    1, 1)
)

fun getDefaultLevel() = Level(getDefaultImmutableLevel())