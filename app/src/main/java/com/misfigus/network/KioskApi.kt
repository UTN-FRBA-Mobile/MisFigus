package com.misfigus.network
import com.misfigus.models.Coordinates
import com.misfigus.models.KioskDTO

object KioskApi {
    fun getKiosks(): List<KioskDTO> {
        return listOf(
            KioskDTO(
                id = "1",
                name = "Kiosco Joel",
                address = "Ceretti 2100",
                coordinates = Coordinates(-34.578660, -58.486447),
                rating = 4.5,
                openUntil = "20:30",
                availableUnits = 5,
                stock = true,
                price = 500
            )
        )
    }
}