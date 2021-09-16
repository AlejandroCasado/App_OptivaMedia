package com.example.optivemedia.model
/*
* Creada por: Alejandro Casado Benito, 2021
 */

data class Pelicula(
    var nombre: String? = null,
    var descripcion: String? = null,
    var duracion: String? = null,
    var palabrasClave: List<String>? = null,
    var tipo: String? = null,
    var calidad: String? = null,
    var externalId: String? = null,
    var urlImagen: String? = null,
    var año: String? = null,
    var isLike: Boolean? = null,
    var votos:String? =null,
    var valoracion:String?=null
)