package com.example.optivemedia.usecase.favoritos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.optivemedia.databinding.ActivityFavoritosBinding
import com.example.optivemedia.model.*
import com.example.optivemedia.model.imp.IFilmImp
import com.example.optivemedia.model.imp.IRepoFilmImp
import com.example.optivemedia.usecase.recycler.RecyclerAdapter
import com.example.optivemedia.usecase.infoPelicula.InfoFillmRouter
import com.example.optivemedia.usecase.listaPeliculas.ListFilmViewModelFactory
import java.util.ArrayList
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class FavoritosActivity : AppCompatActivity(), RecyclerAdapter.OnFunkoClickListener, SearchView.OnQueryTextListener {

    //instancias
    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var search: SearchView
    private var listFilmFavOriginal: ArrayList<Pelicula> = arrayListOf()

    private val lisFavViewModel by lazy {
        ViewModelProvider(
            this,
            ListFilmViewModelFactory(IFilmImp(IRepoFilmImp()))
        ).get(FavoritosViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Películas favoritas")
        setUp()
    }

    private fun setUp() {

        lisFavViewModel.loadingList()
        binding.svBuscar.setOnQueryTextListener(this)
        observerLoadingListFilm()
        observerAddListFilmFav()
        observerDeleteListFilmFav()
    }

    private fun setUpRecycleView(list: ListaPeliculas) {

        binding.rvListaFilms.layoutManager = GridLayoutManager(this, 2)
        binding.rvListaFilms.adapter = RecyclerAdapter(this, list, this)
    }

    //Observadoras
    private fun observerLoadingListFilm() {
        lisFavViewModel.loadingListFilm.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("LISTA_PELICULAS_FAV", "Cargando")

                }
                is Resource.Success -> {

                    listFilmFavOriginal = it.data.lista!!
                    setUpRecycleView(it.data)
                    Log.i("LISTA_PELICULAS_FAV", "correcto")

                }
                is Resource.Failure -> {
                    Log.e("LISTA_PELICULAS_FAV", "Error:${it.exception.message}")

                }
            }
        })

    }
    private fun observerAddListFilmFav() {
        lisFavViewModel.addFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("ADD_FILM_FAV", "Cargando")
                }
                is Resource.Success -> {

                    // setUpLoadingListFavFilm(it.data)
                    if(it.data){
                        Log.i("ADD_FILM_FAV", "correcto")
                    }else{
                        Log.e("ADD_FILM_FAV", "error")
                    }
                }
                is Resource.Failure -> {
                    Log.e("ADD_FILM_FAV", "Error:${it.exception.message}")

                }
            }
        })

    }
    private fun observerDeleteListFilmFav() {
        lisFavViewModel.deleteFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("DELETE_FILM_FAV", "Cargando")
                }
                is Resource.Success -> {
                    if(it.data){
                        lisFavViewModel.loadingList()
                        Log.i("DELETE_FILM_FAV", "correcto")

                    }else{
                        Log.e("DELETE_FILM_FAV", "error")
                    }
                }
                is Resource.Failure -> {
                    Log.e("DELETE_FILM_FAV", "Error:${it.exception.message}")

                }
            }
        })

    }
    override fun onItemClick(film: Pelicula) {

        Toast.makeText(this, "${film.nombre}", Toast.LENGTH_SHORT)
            .show()
        InfoFillmRouter(film.externalId).launch(this)
    }
    override fun onLikeClick(islike: Boolean, film: Pelicula) {
        if (islike) {
            lisFavViewModel.addFilm(film)
        } else {
            lisFavViewModel.deleteFilm(film)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        var pelis = ListaPeliculas()
        pelis.lista = arrayListOf()
        val long = query!!.length
        if (long != 0) {
            listFilmFavOriginal.forEach {
                if (it.nombre!!.lowercase().contains(query.lowercase())) {
                    pelis.lista!!.add(it)
                    // listFilm!!.add(it)
                }
            }
        } else {
            pelis.lista = listFilmFavOriginal
        }
        setUpRecycleView(pelis)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        var pelis = ListaPeliculas()
        pelis.lista = arrayListOf()
        val long = newText!!.length
        if (long != 0) {
            listFilmFavOriginal.forEach {
                if (it.nombre!!.lowercase().contains(newText.lowercase())) {
                    pelis.lista!!.add(it)
                    // listFilm!!.add(it)
                }
            }
        } else {
            pelis.lista = listFilmFavOriginal
        }
        setUpRecycleView(pelis)
        return false
    }


}