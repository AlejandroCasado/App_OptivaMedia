package com.example.optivemedia.model.interfaces

import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import java.io.InputStream
/*
* Creada por: Alejandro Casado Benito, 2021
 */

interface IRepoFilm {
    suspend fun addFilmFav(film: Pelicula): Resource<Boolean>
    suspend fun listFilm(stream: InputStream): Resource<ListaPeliculas>
    suspend fun deleteFilmFav(film: Pelicula): Resource<Boolean>
    suspend fun isFilmFav(list: ListaPeliculas): Resource<ListaPeliculas>
    suspend fun listFilmFav(): Resource<ListaPeliculas>
    suspend fun recoFilm(externalId: String): Resource<ListaPeliculas>
    suspend fun infoFilm(externalId: String): Resource<Pelicula>
}