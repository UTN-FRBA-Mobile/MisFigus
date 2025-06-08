package com.misfigus.models

import androidx.annotation.DrawableRes
import com.example.misfigus.R
import kotlinx.serialization.Serializable

@Serializable
enum class AlbumCategoryEnum(val description: String,var spanishDesc: String,  @DrawableRes val icon: Int) {
    FOOTBALL("FOOTBALL", "Fútbol", R.drawable.football_icon),
    SERIES("SERIES", "Series",R.drawable.series_icon),
    MOVIES("MOVIES", "Películas" ,R.drawable.movies_icon),
    VIDEOGAMES("VIDEOGAMES", "Videojuegos",R.drawable.videogames_icon),
    OTHERS("OTHERS", "Otros",R.drawable.others_icon)

}