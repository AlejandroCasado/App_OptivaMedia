package com.example.optivemedia.usecase.infoPelicula

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optivemedia.model.interfaces.IFilm
import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import com.example.optivemedia.provider.jsonReader.JsonReader
import kotlinx.coroutines.launch
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class InfoFilmViewModel(val operationFilm: IFilm):ViewModel() {

    val infoFilmDet= MutableLiveData<Pelicula>()
    val recoFilmDet= MutableLiveData<ListaPeliculas>()
    val addFilmFav = MutableLiveData<Resource<Boolean>>()
    val deleteFilmFav =  MutableLiveData<Resource<Boolean>>()

    fun infoFilm(externalId: String) {
        viewModelScope.launch {
            try {
                val isAdd = JsonReader().leer(externalId)
                infoFilmDet.postValue(isAdd)
            } catch (e: Exception) {
            }
        }
    }
    fun recoFilm(externalId: String) {
        viewModelScope.launch {
            try {
                val isAdd = JsonReader().leerReco(externalId)
                recoFilmDet.postValue(isAdd)
            } catch (e: Exception) {
            }
        }
    }
    fun addFilm(film: Pelicula) {
        viewModelScope.launch {
            addFilmFav.postValue(Resource.Loading())
            try {
                val isAdd = operationFilm.addFilmFav(film)
                addFilmFav.postValue(isAdd)
            } catch (e: Exception) {
                addFilmFav.postValue(Resource.Failure(e))
            }
        }
    }
    fun deleteFilm(film: Pelicula) {
        viewModelScope.launch {
            deleteFilmFav.postValue(Resource.Loading())
            try {
                val isAdd = operationFilm.deleteFilmFav(film)
                deleteFilmFav.postValue(isAdd)
            } catch (e: Exception) {
                deleteFilmFav.postValue(Resource.Failure(e))
            }
        }
    }
}