package com.example.optivemedia.model.imp

import com.example.optivemedia.model.ListaFavoritos
import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.model.Resource
import com.example.optivemedia.model.interfaces.IRepoFilm
import com.example.optivemedia.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.URL
import java.nio.charset.Charset
import java.util.ArrayList
/*
* Creada por: Alejandro Casado Benito, 2021
 */

class IRepoFilmImp : IRepoFilm {

    override suspend fun addFilmFav(film: Pelicula): Resource<Boolean> {

        val result =
            FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT).get()
                .await()
        val datos = result.toObject<ListaFavoritos>()
        var isAdd = false
        var estaEnLaLista = false
        var i = 0
        if (datos!!.lista != null) {
            datos.lista!!.forEach {
                if (it.containsKey(film.externalId)) {
                    estaEnLaLista = true
                }
                i++
            }
        } else {
            datos.lista = ArrayList<MutableMap<String, Pelicula>>()
        }
        if (!estaEnLaLista) {
            val mu: MutableMap<String, Pelicula> = mutableMapOf()
            mu.put(film.externalId!!, film)
            film.isLike = true
            datos.lista!!.add(mu)

            isAdd = true
            FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT).set(datos)
                .await()
        }


        return Resource.Success(isAdd)
    }

    override suspend fun listFilm(stream: InputStream): Resource<ListaPeliculas> {

        val list: ArrayList<Pelicula> = arrayListOf()
        val json = readData(stream)
        try {
            val obj = JSONObject(json)
            println(obj)
            val myArray = obj.getJSONArray(Constants.RESPONSE)
            var i = 0
            while (i < myArray.length()) {
                val inside = myArray.getJSONObject(i)
                val film = Pelicula()
                val keywords = inside.getString(Constants.KEYWORDS).toString()
                film.palabrasClave = keywords.split(";")

                val urli = inside.getJSONArray(Constants.ATTACHMENTS)
                val x = urli.getJSONObject(0)

                film.urlImagen = x.getString(Constants.VALUE)
                film.nombre = inside.getString(Constants.NAME).toString()
                film.descripcion = inside.getString(Constants.DESCRIPTION).toString()
                film.duracion = inside.getString(Constants.DURATION).toString()
                film.tipo = inside.getString(Constants.TYPE).toString()
                film.calidad = inside.getString(Constants.DEFINITION).toString()
                film.externalId = inside.getString(Constants.EXTERNALID).toString()
                film.año = inside.getString(Constants.YEAR).toString()
                film.isLike = false

                list.add(film)


                i++
            }

        } catch (e: JSONException) {

        }
        val final = ListaPeliculas()
        final.lista = list
        return Resource.Success(final)
    }

    override suspend fun deleteFilmFav(film: Pelicula): Resource<Boolean> {
        val result =
            FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT).get()
                .await()
        val datos = result.toObject<ListaFavoritos>()
        var isDelete = false

        var estaEnLaLista = false
        var aux = 0
        var i = 0
        if (datos!!.lista != null) {
            datos.lista!!.forEach {
                if (it.containsKey(film.externalId)) {
                    estaEnLaLista = true
                    aux = i
                }
                i++
            }
            if (estaEnLaLista) {
                film.isLike = false
                datos.lista!!.removeAt(aux)
                isDelete = true
                FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT)
                    .set(datos).await()
            }
        }
        return Resource.Success(isDelete)
    }

    override suspend fun isFilmFav(list: ListaPeliculas): Resource<ListaPeliculas> {
        val result =
            FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT).get()
                .await()
        val datos = result.toObject<ListaFavoritos>()
        if (datos!!.lista != null) {
            list.lista!!.forEach { f ->
                datos.lista!!.forEach {
                    if (it.containsKey(f.externalId)) {
                        f.isLike = true
                    }
                }

            }
        }
        return Resource.Success(list)
    }

    override suspend fun listFilmFav(): Resource<ListaPeliculas> {
        val result =
            FirebaseFirestore.getInstance().collection(Constants.NAME_COLLECTION).document(Constants.NAME_DOCUMENT).get()
                .await()
        val datos = result.toObject<ListaFavoritos>()
        val list: ArrayList<Pelicula> = arrayListOf()
        if (datos!!.lista != null) {
            datos.lista!!.forEach {
                val key = it.values
                key.forEach {film->
                    list.add(film)
                }
            }
        }
        val films = ListaPeliculas()
        films.lista = list
        return Resource.Success(films)

    }

    override suspend fun recoFilm(externalId: String): Resource<ListaPeliculas> {
        val l = ListaPeliculas()
        return Resource.Success(l)
    }

    override suspend fun infoFilm(externalId: String): Resource<Pelicula> {

        val infoUrl = "https://smarttv.orangetv.orange.es/stv/api/rtv/v1/GetVideo?client=json&external_id=$externalId"



        val test = withContext(Dispatchers.Default){

            try{
                val json: JSONObject =  readJsonFromUrl(infoUrl)
                val inside = json.getJSONObject(Constants.RESPONSE)

                val film = Pelicula()
                val keywords = inside.getString(Constants.KEYWORDS).toString()
                film.palabrasClave = keywords.split(";")

                val urli = inside.getJSONArray(Constants.ATTACHMENTS)
                val f = urli.getJSONObject(0)

                film.urlImagen = f.getString(Constants.VALUE)
                film.nombre = inside.getString(Constants.NAME).toString()
                film.descripcion = inside.getString(Constants.DESCRIPTION).toString()
                film.duracion = inside.getString(Constants.DURATION).toString()
                film.tipo = inside.getString(Constants.TYPE).toString()
                film.calidad = inside.getString(Constants.DEFINITION).toString()
                film.externalId = inside.getString(Constants.EXTERNALID).toString()
                film.año = inside.getString(Constants.YEAR).toString()
                film.isLike = false

                lista(film)
            }catch (e:Exception){
                val f = Pelicula()
                lista(f)
            }

        }

        return Resource.Success(test)

    }

    private fun lista(film : Pelicula): Pelicula {
        return film
    }
    private fun readData(stream: InputStream): String {

        var tContents = ""

        try {
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {

        }
        return tContents
    }


    @Throws(IOException::class, JSONException::class)
    fun readJsonFromUrl(url: String?): JSONObject {
        val x = URL(url).openStream()
        return try {
            val rd =
                BufferedReader(InputStreamReader(x, Charset.forName("UTF-8")))
            val jsonText: String = readAll(rd)
            JSONObject(jsonText)
        } finally {
            x.close()
        }
    }

    @Throws(IOException::class)
    private fun readAll(rd: Reader): String {
        val sb = StringBuilder()
        var cp: Int
        while (rd.read().also { cp = it } != -1) {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }


}