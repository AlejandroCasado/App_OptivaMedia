package com.example.optivemedia.usecase.favoritos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optivemedia.model.interfaces.IFilm
import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import kotlinx.coroutines.launch
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class FavoritosViewModel(val operationFilm: IFilm) :ViewModel() {

    val loadingListFilm = MutableLiveData<Resource<ListaPeliculas>>()
    val addFilmFav = MutableLiveData<Resource<Boolean>>()
    val deleteFilmFav =  MutableLiveData<Resource<Boolean>>()


    fun loadingList() {

        viewModelScope.launch {
            loadingListFilm.postValue(Resource.Loading())
            try{
                val isEmpty = operationFilm.listFilmFav()
                loadingListFilm.postValue(isEmpty)
            }catch (e:Exception){
                loadingListFilm.postValue(Resource.Failure(e))
            }
        }


    }

    fun addFilm(film: Pelicula) {
        viewModelScope.launch {
            addFilmFav.postValue(Resource.Loading())
            try{
                val isEmpty = operationFilm.addFilmFav(film)
                addFilmFav.postValue(isEmpty)
            }catch (e:Exception){
                addFilmFav.postValue(Resource.Failure(e))
            }
        }
    }

    fun deleteFilm(film: Pelicula) {
        viewModelScope.launch {
            deleteFilmFav.postValue(Resource.Loading())
            try{
                val isEmpty = operationFilm.deleteFilmFav(film)
                deleteFilmFav.postValue(isEmpty)
            }catch (e:Exception){
                deleteFilmFav.postValue(Resource.Failure(e))
            }
        }
    }
}