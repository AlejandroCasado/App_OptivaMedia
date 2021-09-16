package com.example.optivemedia.usecase.listaPeliculas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optivemedia.model.interfaces.IFilm
import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import kotlinx.coroutines.launch
import java.io.InputStream
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class ListFilmViewModel(val operationFilm: IFilm) : ViewModel() {

    val addFilmFav = MutableLiveData<Resource<Boolean>>()
    val deleteFilmFav =  MutableLiveData<Resource<Boolean>>()
    val loadingListFilm = MutableLiveData<Resource<ListaPeliculas>>()
    val isFilmFav =  MutableLiveData<Resource<ListaPeliculas>>()

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
    fun loadingList(stream: InputStream) {

        viewModelScope.launch {
            loadingListFilm.postValue(Resource.Loading())
            try{
                val isEmpty = operationFilm.listFilm(stream)
                loadingListFilm.postValue(isEmpty)
            }catch (e:Exception){
                loadingListFilm.postValue(Resource.Failure(e))
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
    fun isListFav(list: ListaPeliculas) {
        viewModelScope.launch {
            isFilmFav.postValue(Resource.Loading())
            try {
                val isFilm = operationFilm.isFilmFav(list)
                isFilmFav.postValue(isFilm)
            } catch (e: Exception) {
                isFilmFav.postValue(Resource.Failure(e))
            }
        }
    }


}