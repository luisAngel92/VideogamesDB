package com.example.videogamesdb.application

import android.app.Application
import com.example.videogamesdb.data.GameRepository
import com.example.videogamesdb.data.db.GameDatabase

class VideogamesDBApp(): Application() {

    private val database by lazy{
        GameDatabase.getDatabase(this@VideogamesDBApp)
    }

    val repository by lazy{
        GameRepository(database.gameDao())
    }

}