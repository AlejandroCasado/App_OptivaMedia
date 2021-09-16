package com.example.optivemedia.usecase.infoPelicula

import android.content.Context
import android.content.Intent
import com.example.optivemedia.usecase.base.BaseRouter
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class InfoFillmRouter(private val externalId: String?) :BaseRouter {
    override fun intent(activity: Context): Intent =Intent(activity,InfoFilmActivity::class.java).apply {
        putExtra("externalId",externalId)
    }
}