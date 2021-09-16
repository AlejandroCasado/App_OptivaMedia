package com.example.optivemedia.usecase.listaPeliculas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.optivemedia.R
import com.example.optivemedia.databinding.ActivityListFilmBinding
import com.example.optivemedia.model.*
import com.example.optivemedia.model.imp.IFilmImp
import com.example.optivemedia.model.imp.IRepoFilmImp
import com.example.optivemedia.usecase.recycler.RecyclerAdapter
import com.example.optivemedia.usecase.favoritos.FavoritosRouter
import com.example.optivemedia.usecase.infoPelicula.InfoFillmRouter
import java.util.ArrayList
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class ListFilmActivity : AppCompatActivity(), RecyclerAdapter.OnFunkoClickListener,SearchView.OnQueryTextListener {

    //instancias
    private lateinit var binding: ActivityListFilmBinding

    private var listFilmOriginal: ArrayList<Pelicula> = arrayListOf()
    private val listFilmViewModel by lazy {
        ViewModelProvider(
            this,
            ListFilmViewModelFactory(IFilmImp(IRepoFilmImp()))
        ).get(ListFilmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MiTema)
        super.onCreate(savedInstanceState)
        binding = ActivityListFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Todas las películas")
        setUp()


    }

    private fun setUp() {
        val file = assets.open("ListaPeliculas.json")
        listFilmViewModel.loadingList(file)
        binding.svBuscar.setOnQueryTextListener(this)
        binding.btnFav.setOnClickListener{
            FavoritosRouter().launch(this)
            onPause()
        }
        observerLoadingListFilm()
        observerLoadingListFilmFav()
        observerAddListFilmFav()
        observerDeleteListFilmFav()

    }


    private fun setUpRecycleView(list:ListaPeliculas) {

        binding.rvListaFilms.layoutManager = GridLayoutManager(this, 2)
        binding.rvListaFilms.adapter = RecyclerAdapter(this, list, this)
    }
    private fun setUpLoadingListFavFilm(list:ListaPeliculas) {
        listFilmViewModel.isListFav(list)
    }

    //Observadoras
    private fun observerLoadingListFilm() {
        listFilmViewModel.loadingListFilm.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("LISTA_PELICULAS_FAV", "Cargando")

                }
                is Resource.Success -> {

                    setUpLoadingListFavFilm(it.data)
                    Log.i("LISTA_PELICULAS_FAV", "correcto")
                }
                is Resource.Failure -> {
                    Log.e("LISTA_PELICULAS_FAV", "Error:${it.exception.message}")

                }
            }
        })

    }
    private fun observerLoadingListFilmFav() {
        listFilmViewModel.isFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("LISTA_PELICULAS", "Cargando")
                }
                is Resource.Success -> {
                    listFilmOriginal = it.data.lista!!
                     setUpRecycleView(it.data)
                    Log.i("LISTA_PELICULAS", "correcto")
                }
                is Resource.Failure -> {
                    Log.e("LISTA_PELICULAS", "Error:${it.exception.message}")
                }
            }
        })

    }
    private fun observerAddListFilmFav() {
        listFilmViewModel.addFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("ADD_FILM_FAV", "Cargando")
                }
                is Resource.Success -> {

                    if(it.data){
                        Log.i("ADD_FILM", "correcto")
                    }else{
                        Log.e("ADD_FILM_", "error al añadir la película a la BBDD")
                    }
                }
                is Resource.Failure -> {
                    Log.e("ADD_FILM", "Error:${it.exception.message}")

                }
            }
        })

    }
    private fun observerDeleteListFilmFav() {
        listFilmViewModel.deleteFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("DELETE_FILM", "Cargando")
                }
                is Resource.Success -> {
                    if(it.data){
                        Log.i("DELETE_FILM", "correcto")

                    }else{
                        Log.e("DELETE_FILM", "error al eliminar la película")
                    }
                }
                is Resource.Failure -> {
                    Log.e("DELETE_FILM", "Error:${it.exception.message}")

                }
            }
        })

    }
    //Acciones
    override fun onItemClick(film: Pelicula) {
        Toast.makeText(this, "${film.nombre}", Toast.LENGTH_SHORT)
            .show()
        InfoFillmRouter(film.externalId).launch(this)
    }
    override fun onLikeClick(islike: Boolean, film: Pelicula) {
        if (islike) {
            listFilmViewModel.addFilm(film)
        } else {
            listFilmViewModel.deleteFilm(film)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val pelis = ListaPeliculas()
        pelis.lista = arrayListOf()
        val long = query!!.length
        if(long != 0){
            listFilmOriginal.forEach {
                if(it.nombre!!.lowercase().contains(query.lowercase())){
                    pelis.lista!!.add(it)
                    // listFilm!!.add(it)
                }
            }
        }else{
            pelis.lista = listFilmOriginal
        }
        setUpRecycleView(pelis)
        return false
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onQueryTextChange(newText: String?): Boolean {


        val pelis = ListaPeliculas()
        pelis.lista = arrayListOf()
        val long = newText!!.length
        if(long != 0){
            listFilmOriginal.forEach {
                if(it.nombre!!.lowercase().contains(newText.lowercase())){
                    pelis.lista!!.add(it)
                   // listFilm!!.add(it)
                }
            }
        }else{
            pelis.lista = listFilmOriginal
        }
        setUpRecycleView(pelis)
        return false
    }

    override fun onResume() {
        super.onResume()
        setUp()
    }
}