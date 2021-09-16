package com.example.optivemedia.model

import java.lang.Exception
/*
* Creada por: Alejandro Casado Benito, 2021
 */

sealed class Resource<out T> {
    class Loading<out T>: Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class  Failure<out T>(val exception: Exception) :Resource<T>()
}