package com.example.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.videogamesdb.R
import com.example.videogamesdb.application.VideogamesDBApp
import com.example.videogamesdb.data.GameRepository
import com.example.videogamesdb.data.db.model.GameEntity
import com.example.videogamesdb.databinding.ActivityMainBinding
import com.example.videogamesdb.databinding.GameDialogBinding
import com.example.videogamesdb.databinding.GameElementBinding
import kotlinx.coroutines.launch
import java.io.IOException

class GameDialog(
    private val newGame: Boolean = true,
    private var game: GameEntity = GameEntity(
        title = "",
        genre = "",
        developer = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment(){

    private lateinit var binding2: GameElementBinding


    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository

    //****************



    //************************

    //se configura el dialogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as VideogamesDBApp).repository





//**********************************************************





        //******************************************
        var spinnerGenre = binding.spinnerGenre
        val generos = arrayOf("Acción", "Plataformas", "Terror", "Deportes", "Peleas")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, generos)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerGenre.adapter = adapter

        binding.spinnerGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = parent?.getItemAtPosition(position).toString()
                // se asigna "selectedGenre" a "game.genre".
               game.genre = selectedGenre





                saveButton?.isEnabled = validateFields()




                //***********************



                //Toast.makeText(requireContext(),"Seleccionaste un genero", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                saveButton?.isEnabled = false
            }
        }


//*********************************************************

        builder = AlertDialog.Builder(requireContext())

        //binding.tietTitle.setText(game.title)
        //binding.tietGenre.setText(game.genre)
        //binding.tietDeveloper.setText(game.developer)

        binding.apply {
            tietTitle.setText(game.title)
            //tietGenre.setText(game.genre)
            tietDeveloper.setText(game.developer)
            //**********************
            spinnerGenre.selectedItem
        }

        dialog = if(newGame){  //si se registra un nuevo juego se asiga el titulo, el genero y desarrollador
            buildDialog("Guardar", "Cancelar", {
                //create (Guardar)
                game.title = binding.tietTitle.text.toString()
                //***********************game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()
                game.genre = binding.spinnerGenre.selectedItem.toString()


                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                    message(getString(R.string.juego_guardado))
                    //Toast.makeText(requireContext(),getString(R.string.juego_guardado), Toast.LENGTH_SHORT).show()

                    //Actualizo la UI
                    updateUI()

                }catch (e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_guardado))
                    //Toast.makeText(requireContext(),getString(R.string.error_guardado), Toast.LENGTH_SHORT).show()
                }
            },{
                //Cancelar

            })

        }else{
            buildDialog("Actualizar", "Borrar", {
               //  update
                game.title = binding.tietTitle.text.toString()
                //*********************game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()
                game.genre = binding.spinnerGenre.selectedItem.toString()

                try {
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }
                    message(getString(R.string.juegoActualizado))
                    //Toast.makeText(requireContext(),"Juego actualizado exitozamente", Toast.LENGTH_SHORT).show()

                    //Actualizo la UI
                    updateUI()

                }catch (e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.errorActualizar))
                    //Toast.makeText(requireContext(),"Error al actualziar el juego", Toast.LENGTH_SHORT).show()
                }

            }, {
                //delete
                AlertDialog.Builder(requireContext())
                    .setTitle("Confimración")
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar"){_, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }
                            message("Juego eliminado")


                            //Actualizo la UI
                            updateUI()

                        }catch (e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el juego")

                        }
                    }
                    .setNegativeButton("Cancelar"){dialog, _ ->
                        dialog.dismiss()

                    }
                    .create()
                    .show()


            })
        }


        return dialog
    }

//Se muestra cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
    _binding = null
    }

//Se llama despues de que se muestra el dialogo en pantalla
    override fun onStart() {
        super.onStart()

    val alertDialog = dialog as AlertDialog //lo usamos para poder emplear el metodo getButton (no lo tiene el dialog)
    saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
    saveButton?.isEnabled = false

    binding.tietTitle.addTextChangedListener(object: TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            saveButton?.isEnabled = validateFields()
        }

    })




    binding.tietDeveloper.addTextChangedListener(object: TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            saveButton?.isEnabled = validateFields()
        }

    })



    }

    private fun validateFields() =
        /* (binding.tietTitle.text.toString().isNotEmpty() && binding.tietGenre.text.toString()
             .isNotEmpty() && binding.tietDeveloper.text.toString().isNotEmpty())*/

    (binding.tietTitle.text.toString().isNotEmpty()  && binding.spinnerGenre.selectedItem.toString().isNotEmpty() && binding.tietDeveloper.text.toString().isNotEmpty())


    private fun buildDialog(btn1Text: String, btn2Text: String, positiveButton: () -> Unit, negativeButton: () -> Unit): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.juego))
            .setPositiveButton(btn1Text){ _, _ ->
                //Acción para el boton positivo
                positiveButton()
            }

            .setNegativeButton(btn2Text){_, _ ->
                //Accion para el boton negativo
                negativeButton()
            }

            .create()
}