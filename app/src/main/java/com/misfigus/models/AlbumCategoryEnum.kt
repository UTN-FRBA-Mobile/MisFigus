package com.misfigus.models

import androidx.annotation.DrawableRes
import com.example.misfigus.R
import kotlinx.serialization.Serializable

@Serializable
enum class AlbumCategoryEnum(val description: String, @DrawableRes val icon: Int) {
    FOOTBALL("FOOTBALL", R.drawable.football_icon),
    SERIES("SERIES", R.drawable.series_icon),
    MOVIES("MOVIES", R.drawable.movies_icon),
    VIDEOGAMES("VIDEOGAMES", R.drawable.videogames_icon),
    OTHERS("OTHERS", R.drawable.others_icon)

}