package com.misfigus.models

import androidx.annotation.DrawableRes
import com.example.misfigus.R
import com.misfigus.navigation.IconType
import kotlinx.serialization.Serializable

@Serializable
enum class AlbumCategoryEnum(val description: String, @DrawableRes val icon: Int) {
    FOOTBALL("Futbol", R.drawable.football_icon),
    SERIES("Series", R.drawable.series_icon),
    MOVIES("Peliculas", R.drawable.movies_icon),
    VIDEOGAMES("Videojuegos", R.drawable.videogames_icon),
    OTHERS("Otros", R.drawable.others_icon)

}