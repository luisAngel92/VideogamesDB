package com.example.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videogamesdb.R
import com.example.videogamesdb.application.VideogamesDBApp
import com.example.videogamesdb.data.GameRepository
import com.example.videogamesdb.data.db.model.GameEntity
import com.example.videogamesdb.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var games: List<GameEntity> = emptyList()
    private lateinit var repository: GameRepository

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //*****************************************



        //*******************************************
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideogamesDBApp).repository

        gameAdapter = GameAdapter(){game ->
            gameClicked(game)
        }

        binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvGames.adapter = gameAdapter



        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch {
            games = repository.getAllGame()

            if(games.isNotEmpty()){
                //hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                //no hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }

            gameAdapter.updateList(games)
        }
    }

    fun click(view: View) {
        val dialog = GameDialog(updateUI = {
            updateUI()
        }, message = {text ->
          message(text)
        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun gameClicked(game: GameEntity){
        //Toast.makeText(this,"click en el juego: ${game.title}", Toast.LENGTH_SHORT).show()

        val dialog = GameDialog(newGame = false, game = game, updateUI = {
            updateUI()
        }, message = {text ->
                message(text)
          })
        dialog.show(supportFragmentManager, "dialog")
    }


    private fun message(text: String){
        Snackbar.make(binding.cl,text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1743"))
            .show()
    }

}