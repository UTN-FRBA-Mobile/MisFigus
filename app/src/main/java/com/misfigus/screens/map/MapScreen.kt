package com.misfigus.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.misfigus.dto.KioskDTO
import com.misfigus.network.KioskApi
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.*
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.misfigus.dto.mappings.KioskAssetsMapper
fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371e3
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var hasLocationPermission by remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    if (!hasLocationPermission) {
        LaunchedEffect(Unit) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    var kiosks by remember { mutableStateOf<List<KioskDTO>>(emptyList()) }
    var selectedKiosk by remember { mutableStateOf<KioskDTO?>(null) }
    var searchText by remember { mutableStateOf("") }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasCenteredMap by remember { mutableStateOf(false) }
    var kioskDetailShown by remember { mutableStateOf<KioskDTO?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(25f) }
    var rating by remember { mutableStateOf(1f) }
    var openNow by remember { mutableStateOf(true) }
    var hasStock by remember { mutableStateOf(true) }
    var showRatingDialog by remember { mutableStateOf(false) }
    var ratingValue by remember { mutableStateOf(0) }
    var ratingComment by remember { mutableStateOf("") }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }


    DisposableEffect(mapView) {
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    LaunchedEffect(Unit) {
        try {
            kiosks = KioskApi.getService(context).getKiosks()
            selectedKiosk = kiosks.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val filteredKiosks = remember(kiosks, searchText, userLocation, radius, rating, openNow, hasStock) {
        val now = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        kiosks.filter { kiosk ->
            val distance = userLocation?.let {
                distanceBetween(it.latitude, it.longitude, kiosk.coordinates.latitude, kiosk.coordinates.longitude)
            } ?: Double.MAX_VALUE

            val isOpen = try {
                val openTime = LocalTime.parse(kiosk.openFrom, formatter)
                val closeTime = LocalTime.parse(kiosk.openUntil, formatter)
                !now.isBefore(openTime) && now.isBefore(closeTime)
            } catch (e: Exception) {
                false
            }
            val matchesSearch = searchText.isBlank() ||
                    kiosk.name.lowercase().removePrefix("kiosco ").contains(searchText.trim().lowercase())

            val withinRadius = userLocation == null || distance / 1000 <= radius
            val meetsRating = kiosk.rating >= rating
            val meetsStock = !hasStock || kiosk.availableUnits > 0
            val meetsOpen = !openNow || isOpen

            matchesSearch && withinRadius && meetsRating && meetsStock && meetsOpen
        }.sortedBy { kiosk ->
            userLocation?.let {
                distanceBetween(it.latitude, it.longitude, kiosk.coordinates.latitude, kiosk.coordinates.longitude)
            } ?: Double.MAX_VALUE
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }) {

            mapView.getMapAsync { googleMap ->
                googleMapInstance = googleMap
                googleMap.uiSettings.isMyLocationButtonEnabled = false
                googleMap.setOnMarkerClickListener { marker ->
                    val kiosk = kiosks.find { it.name == marker.title }
                    if (kiosk != null) {
                        selectedKiosk = kiosk
                        kioskDetailShown = kiosk // <-- esto abre la vista detallada directamente
                    }
                    true
                }
                if (hasLocationPermission) {
                    try {
                        googleMap.isMyLocationEnabled = true
                        if (!hasCenteredMap && userLocation == null) {
                            val fallbackLatLng = LatLng(-34.6037, -58.3816)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fallbackLatLng, 12f))
                        }

                        val locationRequest = LocationRequest.Builder(10000)
                            .setPriority(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY)
                            .setMinUpdateIntervalMillis(5000)
                            .build()

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                val location = locationResult.lastLocation ?: return
                                val latLng = LatLng(location.latitude, location.longitude)
                                userLocation = latLng

                                if (!hasCenteredMap) {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                    hasCenteredMap = true
                                }

                                fusedLocationClient.removeLocationUpdates(this)
                            }
                        }

                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            context.mainLooper
                        )

                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }

            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 90.dp, end = 16.dp), // Ajustá si hay solapamiento con la barra de búsqueda
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = {
                    googleMapInstance?.let {
                        val currentZoom = it.cameraPosition.zoom
                        it.animateCamera(CameraUpdateFactory.zoomTo(currentZoom + 1))
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Text("+", color = Color.White, fontSize = 24.sp)
            }
            IconButton(
                onClick = {
                    googleMapInstance?.let {
                        val currentZoom = it.cameraPosition.zoom
                        it.animateCamera(CameraUpdateFactory.zoomTo(currentZoom - 1))
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Text("-", color = Color.White, fontSize = 24.sp)
            }
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Buscar kiosco...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            trailingIcon = {
                IconButton(onClick = { showFilterSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar"
                    )
                }
            }
            ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(40.dp))
                .align(Alignment.TopCenter),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            shape = RoundedCornerShape(40.dp)
        )


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(filteredKiosks) { kiosk ->
                val hasStock = kiosk.availableUnits > 0
                Card(
                    onClick = {
                        selectedKiosk = kiosk
                        mapView.getMapAsync { map ->
                            val latLng = LatLng(kiosk.coordinates.latitude, kiosk.coordinates.longitude)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(300.dp)
                        .height(92.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(kiosk.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                val isOpen = try {
                                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                                    val now = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).toLocalTime()
                                    val openTime = LocalTime.parse(kiosk.openFrom, formatter)
                                    val closeTime = LocalTime.parse(kiosk.openUntil, formatter)
                                    !now.isBefore(openTime) && now.isBefore(closeTime)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    false
                                }
                                Text(
                                    text = if (isOpen) "Abierto" else "Cerrado",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isOpen) Color(0xFF388E3C) else Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(kiosk.address, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color.DarkGray)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    val color = if (index < kiosk.rating.toInt()) Color(0xFF6A1B9A) else Color.LightGray
                                    Icon(Icons.Default.Star, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${kiosk.rating}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF6A1B9A))
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            modifier = Modifier
                                .width(80.dp)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Indicador de stock
                            if (hasStock) {
                                Text(
                                    text = "Unidades\ndisponibles",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 12.sp
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Sin unidades",
                                        fontSize = 10.sp,
                                        color = Color.Red,
                                        textAlign = TextAlign.Center
                                    )
                                    Canvas(modifier = Modifier.size(10.dp)) {
                                        val strokeWidth = 2.dp.toPx()
                                        drawLine(Color.Red, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth)
                                        drawLine(Color.Red, Offset(size.width, 0f), Offset(0f, size.height), strokeWidth)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Botón "Ver detalles"
                            Button(
                                onClick = { kioskDetailShown = kiosk },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(28.dp),
                                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    "Ver detalles",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
        kioskDetailShown?.let { kiosk ->
            val backgroundImage = remember(kiosk.name) {
                KioskAssetsMapper.getBackgroundImage(context, kiosk.name)
            }
            val kiosqueroImage = remember(kiosk.name) {
                KioskAssetsMapper.getKiosqueroImage(context, kiosk.name)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .clickable { kioskDetailShown = null }
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(470.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = backgroundImage),
                                contentDescription = "Foto del kiosco",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.BottomStart)
                                    .offset(x = 16.dp, y = 32.dp)
                                    .background(Color.White, shape = CircleShape)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = kiosqueroImage),
                                    contentDescription = "Foto del responsable",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 12.dp, end = 12.dp)
                            ) {
                                Text(
                                    text = kiosk.name,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = kiosk.address,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Responsable: ${kiosk.responsible}",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .offset(y = (-40).dp)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Spacer(modifier = Modifier.height(30.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(5) { index ->
                                        val color = if (index < kiosk.rating.toInt()) Color(0xFF6A1B9A) else Color.LightGray
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${kiosk.rating}",
                                        fontSize = 14.sp,
                                        color = Color(0xFF6A1B9A)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Button(
                                        onClick = { showRatingDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                                        modifier = Modifier
                                            .width(70.dp)
                                            .height(32.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(
                                            text = "Valorar",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Hoy abierto hasta las ${kiosk.openUntil} hs", color = MaterialTheme.colorScheme.primary)
                            Text("Horario semanal:", fontWeight = FontWeight.Bold)
                            val days = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
                            val todayIndex = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).dayOfWeek.value % 7
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    days.forEachIndexed { index, day ->
                                        val isToday = index == todayIndex
                                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                            Text(
                                                text = "$day:",
                                                modifier = Modifier.width(90.dp),
                                                color = if (isToday) MaterialTheme.colorScheme.primary else Color(0xFF444444),
                                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                text = "${kiosk.openFrom} - ${kiosk.openUntil}",
                                                color = if (isToday) MaterialTheme.colorScheme.primary else Color.Gray,
                                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .width(120.dp)
                                        .height(90.dp)
                                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "QUEDAN\nPOCAS\nUNIDADES",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🛒 Precio por paquete: \$${kiosk.price}",
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = "Volver",
                                    color = Color(0xFF6A1B9A),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .clickable { kioskDetailShown = null }
                                        .padding(start = 12.dp)
                                )
                            }

                        }
                    }
                }
            }
        }

    }
    if (showFilterSheet) {
        Dialog(onDismissRequest = { showFilterSheet = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Filtro de búsqueda", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                    Spacer(Modifier.height(12.dp))
                    Text("Radio (km)", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)) {
                        val sliderWidth = maxWidth
                        val thumbPosition = (radius - 1f) / 49f * sliderWidth.value
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .offset(x = thumbPosition.dp -15.dp, y = (-28).dp)
                                    .align(Alignment.TopStart)
                                    .background(Color(0xFF6A1B9A), shape = RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(radius.toInt().toString(), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(modifier = Modifier.fillMaxWidth().height(8.dp).align(Alignment.Center)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val y = size.height / 2
                                val barHeight = 5.dp.toPx()

                                drawLine(Color(0x336A1B9A), Offset(0f, y), Offset(size.width, y), barHeight)
                                drawLine(Color(0xFF6A1B9A), Offset(0f, y), Offset((radius - 1f) / 49f * size.width, y), barHeight)
                            }
                        }
                        Slider(
                            value = radius,
                            onValueChange = { radius = it },
                            valueRange = 1f..50f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Transparent,
                                activeTrackColor = Color.Transparent,
                                inactiveTrackColor = Color.Transparent
                            )
                        )
                        Box(
                            modifier = Modifier
                                .offset(x = thumbPosition.dp - 12.dp, y = (12).dp)
                                .size(24.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF6A1B9A))
                                .align(Alignment.TopStart)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Puntuación", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        val sliderWidth = maxWidth
                        val thumbPosition = (rating - 1f) / 4f * sliderWidth.value
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .offset(x = thumbPosition.dp - 15.dp, y = (-28).dp)
                                    .align(Alignment.TopStart)
                                    .background(Color(0xFF6A1B9A), shape = RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = rating.toInt().toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Box(modifier = Modifier.fillMaxWidth().height(8.dp).align(Alignment.Center)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val y = size.height / 2
                                val barHeight = 5.dp.toPx()

                                drawLine(Color(0x336A1B9A), Offset(0f, y), Offset(size.width, y), barHeight)
                                drawLine(Color(0xFF6A1B9A), Offset(0f, y), Offset((rating - 1f) / 4f * size.width, y), barHeight)
                            }
                        }
                        Slider(
                            value = rating,
                            onValueChange = {
                                rating = it.roundToInt().toFloat()
                            },
                            valueRange = 1f..5f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.Transparent,
                                activeTrackColor = Color.Transparent,
                                inactiveTrackColor = Color.Transparent
                            )
                        )
                        Box(
                            modifier = Modifier
                                .offset(x = thumbPosition.dp - 12.dp, y = (12).dp)
                                .size(24.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF6A1B9A))
                                .align(Alignment.TopStart)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ahora abierto", modifier = Modifier.weight(1f), color = Color.Gray)
                        Switch(checked = openNow, onCheckedChange = { openNow = it })
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tiene stock disponible", modifier = Modifier.weight(1f), color = Color.Gray)
                        Switch(checked = hasStock, onCheckedChange = { hasStock = it })
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = {
                                radius = 50f
                                rating = 1f
                                openNow = false
                                hasStock = false
                                showFilterSheet = false
                            }
                        ) {
                            Text("Limpiar filtros", color = Color.Gray)
                        }

                        Button(
                            onClick = { showFilterSheet = false }
                        ) {
                            Text("Aplicar")
                        }
                    }

                }
            }
        }
    }
    if (showRatingDialog && kioskDetailShown != null) {
        val backgroundImage = KioskAssetsMapper.getBackgroundImage(context, kioskDetailShown!!.name)
        val kiosqueroImage = KioskAssetsMapper.getKiosqueroImage(context, kioskDetailShown!!.name)

        Dialog(onDismissRequest = { showRatingDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 500.dp)

            ) {
                Box {
                    Image(
                        painter = painterResource(id = backgroundImage),
                        contentDescription = "Fondo del kiosco",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                    Image(
                        painter = painterResource(id = kiosqueroImage),
                        contentDescription = "Responsable",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    IconButton(
                        onClick = { showRatingDialog = false },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 180.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = kioskDetailShown!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(5) { index ->
                                IconButton(onClick = { ratingValue = index + 1 }) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < ratingValue) Color(0xFF6A1B9A) else Color.LightGray,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = ratingComment,
                            onValueChange = {
                                if (it.length <= 250) ratingComment = it
                            },
                            label = { Text("Contanos tu opinion") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            maxLines = 6,
                            supportingText = {
                                Text("${ratingComment.length} / 250 caracteres usados", modifier = Modifier.align(Alignment.End))
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                showRatingDialog = false
                                ratingValue = 0
                                ratingComment = ""
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                        ) {
                            Text("Enviar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(filteredKiosks) {
        mapView.getMapAsync { map ->
            map.clear()

            filteredKiosks
                .filter { it.coordinates.latitude != 0.0 && it.coordinates.longitude != 0.0 }
                .forEach { kiosk ->
                    val position = LatLng(kiosk.coordinates.latitude, kiosk.coordinates.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(kiosk.name)
                            .snippet("${kiosk.address} | ⭐ ${kiosk.rating} | \$${kiosk.price}")
                    )
                }
        }
    }
}
