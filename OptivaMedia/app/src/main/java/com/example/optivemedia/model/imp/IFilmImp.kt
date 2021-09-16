package com.example.optivemedia.model.imp

import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import com.example.optivemedia.model.interfaces.IFilm
import com.example.optivemedia.model.interfaces.IRepoFilm
import java.io.InputStream
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class IFilmImp(val repo: IRepoFilm): IFilm {
    override suspend fun addFilmFav(film: Pelicula): Resource<Boolean> = repo.addFilmFav(film)
    override suspend fun listFilm(stream: InputStream): Resource<ListaPeliculas> = repo.listFilm(stream)
    override suspend fun deleteFilmFav(film: Pelicula): Resource<Boolean> = repo.deleteFilmFav(film)
    override suspend fun isFilmFav(list: ListaPeliculas): Resource<ListaPeliculas> =repo.isFilmFav(list)
    override suspend fun listFilmFav(): Resource<ListaPeliculas> = repo.listFilmFav()
    override suspend fun recoFilm(externalId: String): Resource<ListaPeliculas> = repo.recoFilm(externalId)
    override suspend fun infoFilm(externalId: String): Resource<Pelicula> =repo.infoFilm(externalId)
}