package com.example.optivemedia.usecase

import android.view.View
import androidx.recyclerview.widget.RecyclerView
/*
* Creada por: Alejandro Casado Benito, 2021
 */

/* Para crear el adaptador*/
abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView){

    abstract fun bind(item: T, position:Int)
}