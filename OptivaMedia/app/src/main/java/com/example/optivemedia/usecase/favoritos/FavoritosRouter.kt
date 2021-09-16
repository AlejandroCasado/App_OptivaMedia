package com.example.optivemedia.usecase.favoritos

import android.content.Context
import android.content.Intent
import com.example.optivemedia.usecase.base.BaseRouter
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class FavoritosRouter:BaseRouter {
    override fun intent(activity: Context): Intent = Intent(activity,FavoritosActivity::class.java)
}