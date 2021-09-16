package com.example.optivemedia.usecase.listaPeliculas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.optivemedia.model.interfaces.IFilm
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class ListFilmViewModelFactory(private val useCase: IFilm):ViewModelProvider.Factory {
    override fun<T: ViewModel?>create(modelClass: Class<T>):T{
        return modelClass.getConstructor(IFilm::class.java).newInstance(useCase)
    }
}