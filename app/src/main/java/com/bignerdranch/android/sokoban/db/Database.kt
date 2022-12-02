package com.bignerdranch.android.sokoban.db

import android.content.Context
import androidx.room.Room


object Database {
    private const val DATABASE_FILENAME = "Sokoban.sqlite"
    private lateinit var db: DatabaseSchema

    val highScoreDao get() = db.highScoreDao()
    val gameStateDao get() = db.gameStateDao()

    fun initDb(ctx: Context) {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(ctx, DatabaseSchema::class.java, DATABASE_FILENAME).build()
        }
    }
}
