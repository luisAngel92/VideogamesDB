package com.example.videogamesdb.data.db

import androidx.room.*
import com.example.videogamesdb.data.db.model.GameEntity
import com.example.videogamesdb.util.Constants.DATABASE_GAME_TABLE


@Dao
interface GameDao {

    //Create
    @Insert
    suspend fun insertGame(game: GameEntity)




    //Read
    @Query("SELECT * FROM ${DATABASE_GAME_TABLE}")
    suspend fun getAllGames(): List<GameEntity>


    //Update
    @Update
    suspend fun updateGame(game: GameEntity)


    //Delete
    @Delete
    suspend fun deleteGame(game: GameEntity)
}