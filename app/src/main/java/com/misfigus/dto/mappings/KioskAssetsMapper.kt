package com.misfigus.dto.mappings

import android.annotation.SuppressLint
import android.content.Context
import com.example.misfigus.R

object KioskAssetsMapper {

    @SuppressLint("DiscouragedApi")
    fun getKiosqueroImage(context: Context, kioskName: String): Int {
        val resourceName = normalizeName(kioskName)
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: R.drawable.kiosco_joel
    }

    @SuppressLint("DiscouragedApi")
    fun getBackgroundImage(context: Context, kioskName: String): Int {
        val resourceName = "background_${normalizeName(kioskName)}"
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: R.drawable.kiosco_plaza
    }

    private fun normalizeName(name: String): String {
        return name.trim().lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "") // elimina tildes, símbolos, etc.
    }
}
