package com.bignerdranch.android.sokoban

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bignerdranch.android.sokoban.logic.Sprite

object Textures {
    private lateinit var wall: Bitmap
    private lateinit var box: Bitmap
    private lateinit var ground: Bitmap
    private lateinit var point: Bitmap
    lateinit var playerD: Bitmap
    lateinit var playerU: Bitmap
    lateinit var playerL: Bitmap
    lateinit var playerR: Bitmap
    private lateinit var boxOnPoint: Bitmap

    fun init(context: Context) {
        wall = BitmapFactory.decodeResource(context.resources, R.drawable.wall)
        box = BitmapFactory.decodeResource(context.resources, R.drawable.box_game)
        ground = BitmapFactory.decodeResource(context.resources, R.drawable.ground)
        point = BitmapFactory.decodeResource(context.resources, R.drawable.point)
        playerD = BitmapFactory.decodeResource(context.resources, R.drawable.player_front)
        playerU = BitmapFactory.decodeResource(context.resources, R.drawable.player_back)
        playerL = BitmapFactory.decodeResource(context.resources, R.drawable.player_left)
        playerR = BitmapFactory.decodeResource(context.resources, R.drawable.player_right)
        boxOnPoint = BitmapFactory.decodeResource(context.resources, R.drawable.box_win)
    }

    fun tile(tile: Char) = when (tile) {
        Sprite.wall -> wall
        Sprite.box -> box
        Sprite.endpoint -> point
        Sprite.boxOnPoint -> boxOnPoint
        Sprite.ground -> ground
        else -> {
            ground
        }
    }


}
