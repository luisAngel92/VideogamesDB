package com.example.videogamesdb.data

import com.example.videogamesdb.data.db.GameDao
import com.example.videogamesdb.data.db.model.GameEntity

class GameRepository(private val gameDao: GameDao) {

    suspend fun insertGame(game: GameEntity){
        gameDao.insertGame(game)
    }

    suspend fun getAllGame(): List<GameEntity> = gameDao.getAllGames() //esto es como un return

    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: GameEntity){
        gameDao.deleteGame(game)
    }



}