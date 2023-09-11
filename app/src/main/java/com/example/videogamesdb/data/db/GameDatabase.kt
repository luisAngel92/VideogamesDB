package com.example.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.videogamesdb.data.db.model.GameEntity
import com.example.videogamesdb.util.Constants


@Database(
    entities = [GameEntity::class],
    version = 1,  //version de la DB importante para las migraciones,  automigrations
    exportSchema = true  // por defecto ya es true
)

abstract class GameDatabase: RoomDatabase() { //Tiene que ser abstracta

    //Aqui va el DAO
    abstract fun gameDao(): GameDao


    //Sin inyeccion de dependencias, metemos la creacion de la db con un singleton aqui
    companion object{
        @Volatile //lo que se escriba en este campo, será inmediatamente visible para otros hilos
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase{
            return INSTANCE?: synchronized(this){
                //si la instancia no es nula se regresa el valor de la instancia y si es nula se regresa la base de datos
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    Constants.DATABASE_NAME

                ).fallbackToDestructiveMigration() //Permite a room recrear las tablas de la DB si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }

}