package com.example.optivemedia.usecase.base

import android.content.Context
import android.content.Intent
/*
* Creada por: Alejandro Casado Benito, 2021
 */

interface BaseRouter {
    // Activity
    fun intent(activity: Context): Intent
    fun launch(activity: Context) = activity.startActivity(intent(activity))
}