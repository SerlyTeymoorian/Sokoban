package com.bignerdranch.android.sokoban

import android.app.Application
import com.bignerdranch.android.sokoban.db.Database


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDatabase()
        setupTextures()
    }
    private fun setupDatabase() {
        Database.initDb(this)
    }
    private fun setupTextures() {
        Textures.init(this)
    }
}