package com.bignerdranch.android.sokoban.logic

object Sprite {

    const val wall = 'w'
    const val box = 'b'
    const val ground = 'g'
    const val endpoint = 'p'
    const val grass = '*'
    const val boxOnPoint = '!'
    private const val GRASS_MASK = 0
    private const val SOLID_MASK = 1
    private const val GROUND_MASK = 2
    private const val ENDPOINT_MASK = 4
    private const val CRATE_MASK = 8
    const val WALKABLE_MASK = 0

    fun isWalkable(c: Char) = mask(c) and SOLID_MASK == WALKABLE_MASK
    fun isCrate(c: Char) = mask(c) and CRATE_MASK == CRATE_MASK

    private fun mask(c: Char): Int = when (c) {
        wall -> SOLID_MASK
        box -> GROUND_MASK or CRATE_MASK or SOLID_MASK
        ground -> GROUND_MASK
        endpoint -> GROUND_MASK or ENDPOINT_MASK
        grass -> GRASS_MASK
        boxOnPoint -> mask(endpoint) or mask(box)
        else -> {
            WALKABLE_MASK
        }
    }

    fun maskToChar(m: Int): Char {
        val tiles = charArrayOf(wall, box, ground, endpoint, grass, boxOnPoint)
        tiles
            .filter { m == mask(it) }
            .forEach { return it }
       // Timber.e("Tile.maskToChar: Unknown mask $m")
        return grass
    }

    fun isGrass(c: Char) = c == grass
    private fun crateMask() = CRATE_MASK or SOLID_MASK

    fun withoutCrate(tile: Char) = maskToChar(mask(tile) xor crateMask())
    fun withCrate(tile: Char) = maskToChar(mask(tile) or crateMask())
}
