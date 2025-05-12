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
            ),
            KioskDTO(
                id = "2",
                name = "Kiosco Plaza",
                address = "Av. Rivadavia 4500",
                coordinates = Coordinates(-34.618160, -58.421400),
                rating = 4.2,
                openUntil = "21:00",
                availableUnits = 8,
                stock = true,
                price = 480
            ),
            KioskDTO(
                id = "3",
                name = "Kiosco Parque",
                address = "Carlos Calvo 1500",
                coordinates = Coordinates(-34.620700, -58.389000),
                rating = 4.7,
                openUntil = "22:00",
                availableUnits = 10,
                stock = true,
                price = 520
            ),
            KioskDTO(
                id = "4",
                name = "Kiosco San Martín",
                address = "San Martín 300",
                coordinates = Coordinates(-34.600000, -58.370000),
                rating = 4.4,
                openUntil = "19:30",
                availableUnits = 3,
                stock = true,
                price = 490
            ),
            KioskDTO(
                id = "5",
                name = "Kiosco Corrientes",
                address = "Av. Corrientes 2500",
                coordinates = Coordinates(-34.603500, -58.410000),
                rating = 4.6,
                openUntil = "23:00",
                availableUnits = 12,
                stock = true,
                price = 530
            )
        )
    }
}