package com.example.optivemedia.usecase.recycler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.optivemedia.R
import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.usecase.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class RecyclerAdapter(
    private val context: Context?,
    private val listFilm: ListaPeliculas?,
    private val itemClickListener: OnFunkoClickListener?
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {




    interface OnFunkoClickListener {
        fun onItemClick(film:Pelicula)
        fun onLikeClick(islike: Boolean,film: Pelicula)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return FilmViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is FilmViewHolder -> holder.bind(listFilm!!.lista!![position], position)
            else -> throw IllegalArgumentException("Se olvido de pasar el viewHolder en el bind")
        }
    }

    override fun getItemCount(): Int = listFilm!!.lista!!.size



    inner class FilmViewHolder(itemView: View) : BaseViewHolder<Pelicula>(itemView) {

        override fun bind(item: Pelicula, position: Int) {

            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(item)
            }

            //Cuando das al boton de agregar lista deseos
            itemView.ivLike.setOnClickListener {

                animation(itemView.ivLike, R.raw.apple_event,false,R.drawable.like)
                itemClickListener!!.onLikeClick(true,item)
            }
            //Cuando das al boton de eliminar lista deseos
            itemView.ivLike.setOnLongClickListener {
                animation(itemView.ivLike, R.raw.apple_event, true,R.drawable.like)
                itemClickListener!!.onLikeClick(false,item)
                true
            }


            val url = "https://smarttv.orangetv.orange.es/stv/api/rtv/v1/images${item.urlImagen}"
            Picasso.get().load(url).into(itemView.ivImagen)
            itemView.tvName.text = item.nombre
            itemView.tvAño.text = "Año: ${item.año}"
            itemView.tvTipo.text = "Tipo: ${item.tipo}"
            //parametros invisibles
            itemView.tvDescripcion.text = item.descripcion
            itemView.tvUrlImage.text = item.urlImagen
            itemView.tvDuracion.text = item.duracion
            itemView.tvCalidad.text = item.calidad
            itemView.tvExternalId.text = item.externalId



            if(item.isLike == true){
                animation(itemView.ivLike,R.raw.apple_event,false,R.drawable.like)
            }else{
                animation(itemView.ivLike,R.raw.apple_event,true,R.drawable.like)

            }

        }



        fun animation(imageView: LottieAnimationView, animation: Int, like: Boolean, imageOld:Int) {
            if(!like){
                aparecerAnimacion(imageView, animation)
            }else{
                desaparecerAnimacion(imageView,imageOld)
            }
        }

        //Funciones para dar animacion a los botones add y like
        private fun aparecerAnimacion(imageView: LottieAnimationView, animation: Int) {
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }

        private fun desaparecerAnimacion(imageView: LottieAnimationView, imageOld: Int) {
            imageView.animate()
                .alpha(0f)
                .setDuration(400)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(imageOld)
                        imageView.alpha = 1f
                    }
                })
        }
    }
}