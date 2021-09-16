package com.example.optivemedia.usecase.infoPelicula

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.optivemedia.databinding.ActivityInfoFilmBinding
import com.example.optivemedia.model.*
import com.example.optivemedia.model.imp.IFilmImp
import com.example.optivemedia.model.imp.IRepoFilmImp
import com.example.optivemedia.usecase.recycler.RecyclerAdapter
import com.example.optivemedia.usecase.listaPeliculas.ListFilmViewModelFactory
import com.squareup.picasso.Picasso
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class InfoFilmActivity : AppCompatActivity(), RecyclerAdapter.OnFunkoClickListener {
    //instancias
    private lateinit var binding: ActivityInfoFilmBinding

    private val infoFilmViewModel by lazy {
        ViewModelProvider(
            this,
            ListFilmViewModelFactory(IFilmImp(IRepoFilmImp()))
        ).get(InfoFilmViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val externalId = bundle?.getString("externalId")

        setTitle("Detalle de la película")

        setUp(externalId!!)
    }
    private fun setUp(externalId: String){

        infoFilmViewModel.infoFilm(externalId)
        observerLoadingListFilmFav()
        infoFilmViewModel.recoFilm(externalId)
        observerRecyclerReco()

    }

    //observadoras
    private fun observerLoadingListFilmFav() {
        infoFilmViewModel.infoFilmDet.observe(this, Observer {
            setUpInfo(it)
            println(it)
        })

    }
    private fun observerRecyclerReco() {
        infoFilmViewModel.recoFilmDet.observe(this, Observer {
            setUpRecycler(it)
        })

    }
    private fun observerAddListFilmFav() {
        infoFilmViewModel.addFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("ADD_FILM_INFO", "Cargando")
                }
                is Resource.Success -> {

                    // setUpLoadingListFavFilm(it.data)
                    if(it.data){
                        Log.i("ADD_FILM_INFO", "correcto")
                    }else{
                        Log.e("ADD_FILM_INFO", "error al añadir la película a la BBDD")
                    }
                }
                is Resource.Failure -> {
                    Log.e("ADD_FILM_INFO", "Error:${it.exception.message}")

                }
            }
        })

    }
    private fun observerDeleteListFilmFav() {
        infoFilmViewModel.deleteFilmFav.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.i("DELETE_FILM_INFO", "Cargando")
                }
                is Resource.Success -> {
                    if(it.data){
                        Log.i("DELETE_FILM_INFO", "correcto")

                    }else{
                        Log.e("DELETE_FILM_INFO", "error al eliminar la película")
                    }
                }
                is Resource.Failure -> {
                    Log.e("DELETE_FILM_INFO ", "Error:${it.exception.message}")

                }
            }
        })

    }
    @SuppressLint("SetTextI18n")
    private fun setUpInfo(film: Pelicula){
        val url = "https://smarttv.orangetv.orange.es/stv/api/rtv/v1/images${film.urlImagen}"
        Picasso.get().load(url).into(binding.ivImagen)
        binding.tvName.text = film.nombre
        binding.tvAO.text = "Año: ${film.año}"
        binding.tvDescripcion.text = film.descripcion
        val mili = film.duracion!!.toInt()
        val time = calcularTiempo(mili/1000)
        binding.tvDuracion.text = "Duración: ${time.toString()}"
        binding.tvCalidad.text = "Calidad: ${film.calidad}"
        binding.tvRaVo.text = "Valoración: ${film.valoracion} Votos: ${film.votos}"
    }
    private fun setUpRecycler(list: ListaPeliculas) {

        binding.rvListaFilms.layoutManager = GridLayoutManager(this, 2)
        binding.rvListaFilms.adapter = RecyclerAdapter(this, list, this)
    }

    private fun calcularTiempo(tsegundos: Int): String? {
        val horas: Int = tsegundos / 3600
        val minutos: Int = (tsegundos - horas * 3600) / 60
        val segundos: Int = tsegundos - (horas * 3600 + minutos * 60)
        return horas.toString() + "hr :" + minutos.toString() + "min:" + segundos.toString() + "seg"
    }
    override fun onItemClick(film: Pelicula) {
        Toast.makeText(this, "${film.nombre}", Toast.LENGTH_SHORT)
            .show()
        InfoFillmRouter(film.externalId).launch(this)
    }
    override fun onLikeClick(islike: Boolean, film: Pelicula) {
        if (islike) {
            infoFilmViewModel.addFilm(film)
        } else {
            infoFilmViewModel.deleteFilm(film)
        }
    }
}