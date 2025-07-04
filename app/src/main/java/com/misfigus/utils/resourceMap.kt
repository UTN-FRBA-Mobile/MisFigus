import com.example.misfigus.R


fun getUserProfilePictureId(userId: String): Int {
    val userPictureMap = mapOf(
        "marcos" to R.drawable.marcos,
        "pedro" to R.drawable.pedro,
        "matias" to R.drawable.matias,
        "valeria" to R.drawable.valeria
    )
    return userPictureMap[userId] ?: R.drawable.unknown
}

