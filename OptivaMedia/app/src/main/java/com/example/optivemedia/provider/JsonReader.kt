package com.example.optivemedia.provider.jsonReader

import com.example.optivemedia.model.ListaPeliculas
import com.example.optivemedia.model.Pelicula
import com.example.optivemedia.util.Constants
import kotlinx.coroutines.Dispatchers
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

class JsonReader() {


    suspend fun leer(externalId: String): Pelicula {

        val p = withContext(Dispatchers.Default) {
            infoFilm(externalId)
        }
        return p
    }

    suspend fun leerReco(externalId: String): ListaPeliculas {

        val p = withContext(Dispatchers.Default) {
            recoFilm(externalId)
        }
        return p
    }


    suspend fun infoFilm(externalId: String): Pelicula {

        val infoUrl =
            "https://smarttv.orangetv.orange.es/stv/api/rtv/v1/GetVideo?client=json&external_id=$externalId"

        val test = withContext(Dispatchers.Default) {

            try {

                val json: JSONObject = readJsonFromUrl(infoUrl)
                val inside = json.getJSONObject(Constants.RESPONSE)

                val g = inside.getJSONArray(Constants.METADATA)
                val t = g.getJSONObject(2)
                val lo = t.getString(Constants.VALUE)

                val infoRatVo = lo.split('"')
                val rating = infoRatVo[5]
                val votes = infoRatVo[9]
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
                film.votos = votes
                film.valoracion = rating
                lista(film)
            } catch (e: Exception) {
                val f = Pelicula()
                lista(f)
            }
        }
        return test

    }

    private fun lista(film: Pelicula): Pelicula {
        return film
    }
    private fun lista2(listaFilm: ListaPeliculas): ListaPeliculas {
        return listaFilm
    }
    suspend fun recoFilm(externalId: String): ListaPeliculas {


        val sep = externalId.split("_")
        val id = "${sep[0]}_${sep[1]}"
        val recoUrl =
            "https://smarttv.orangetv.orange.es/stv/api/reco/v1/GetVideoRecommendationList?client=json&type=all&subscription=false&filter_viewed_content=true&max_results=10&blend=ar_od_blend_2424video&params=external_content_id:${id}&max_pr_level=8&quality=SD,HD&services=2424VIDEO"

        val test = withContext(Dispatchers.Default) {

            try {
                val list: ArrayList<Pelicula> = arrayListOf()
                val json: JSONObject = readJsonFromUrl(recoUrl)
                val myArray = json.getJSONArray(Constants.RESPONSE)

                var i = 0
                while (i < myArray.length()) {
                    val inside = myArray.getJSONObject(i)
                    val id = inside.getString(Constants.EXTERNALCONTENTID)
                    val idFinish = "${id}_PAGE_HD"
                    val fi = leer(idFinish)
                    list.add(fi)
                    i++
                }
                val listaFilms = ListaPeliculas()
                listaFilms.lista = list
                lista2((listaFilms))

            } catch (e: Exception) {
                val listaFilms = ListaPeliculas()
                val list: ArrayList<Pelicula>? = arrayListOf()
                listaFilms.lista = list
                lista2(listaFilms)

            }
        }
        return test

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