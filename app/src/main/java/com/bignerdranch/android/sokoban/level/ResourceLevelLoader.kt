package com.bignerdranch.android.sokoban.level

import android.content.Context
import com.bignerdranch.android.sokoban.logic.Pos
import java.io.IOException
import java.io.InputStream
import java.util.*

object ResourceLevelLoader : LevelLoader {

    @Throws(IOException::class)
    override fun load(context: Context, levelNumber: Int): ImmutableLevel {
        //get the file for corresponding level
        val levelFileName = "listOfLevels/$levelNumber.level"
        val inputStream: InputStream = context.assets.open(levelFileName)
        val scanner = Scanner(inputStream)
        //first input in the file is the height (# of rows)
        val height = scanner.nextInt()
        //2nd input in the file is the width (# of columns)
        val width = scanner.nextInt()
        //we get the player position by reading the nxt line (x and y position)
        val playerPos = Pos(scanner.nextInt(), scanner.nextInt())
        //create an array characters with the size width*height
        val data = CharArray(width * height)

        //read the rest of the file which is the game view
        for (i in 0 until height) {
            val line = scanner.next()
            System.arraycopy(line.toCharArray(), 0, data, i * width, line.length)
        }
        scanner.close()
        //pass in the width with all the data and player position
        return ImmutableLevel(data, width, playerPos)
    }
}
