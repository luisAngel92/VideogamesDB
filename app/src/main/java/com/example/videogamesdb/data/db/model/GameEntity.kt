package com.example.videogamesdb.data.db.model

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.videogamesdb.util.Constants


@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    val id: Long = 0,

    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "game_genre")
    var genre: String,

    @ColumnInfo(name = "game_developer")
    var developer: String

    )
