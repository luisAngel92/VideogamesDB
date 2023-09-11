package com.example.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videogamesdb.R
import com.example.videogamesdb.data.db.model.GameEntity
import com.example.videogamesdb.databinding.GameElementBinding

class GameAdapter(private val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var games: List<GameEntity> = emptyList()


    private val genreToDrawableMap = mapOf(
        "Acción" to R.drawable.accion,
        "Plataformas" to R.drawable.plataformas,
        "Terror" to R.drawable.terror,
        "Peleas" to R.drawable.peleas,
        "Deportes" to R.drawable.deportes,
        // Agrega más géneros y recursos de imagen según sea necesario
    )

    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivIcon = binding.ivIcon

        fun bind(game: GameEntity){


            binding.apply {
                tvTitle.text = game.title
                tvGenre.text = game.genre
                tvDeveloper.text = game.developer

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])

        holder.itemView.setOnClickListener{
            //aqui va el click del elemento
            onGameClick(games[position])
        }

        val genre = games[position].genre
        // Obtén el recurso de imagen correspondiente al género
        val drawableRes = genreToDrawableMap[genre]
        // Asigna el recurso de imagen a ivIcon
        holder.ivIcon.setImageResource(drawableRes ?: R.drawable.gamepad)

        holder.ivIcon.setOnClickListener{
            //click para la vista del imageview con el icono
        }

    }

    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()

    }


}