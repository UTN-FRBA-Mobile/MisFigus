package com.misfigus.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

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

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }) {
            mapView.getMapAsync { googleMap: GoogleMap ->
                // Lista de kioscos simulados
                val kiosks = listOf(
                    LatLng(-34.6030, -58.3820),
                    LatLng(-34.6050, -58.3800),
                    LatLng(-34.6070, -58.3840)
                )
                // Centrado inicial por defecto (CABA)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.6037, -58.3816), 12f))
                // Agregar marcadores de kioscos
                kiosks.forEach { kioskLocation ->
                    googleMap.addMarker(
                        com.google.android.gms.maps.model.MarkerOptions()
                            .position(kioskLocation)
                            .title("Kiosco disponible")
                    )
                }
                if (hasLocationPermission) {
                    try {
                        googleMap.isMyLocationEnabled = true

                        val locationRequest = LocationRequest.Builder(10000)
                            .setPriority(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY)
                            .setMinUpdateIntervalMillis(5000)
                            .build()

                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                val location = locationResult.lastLocation ?: return
                                val userLocation = LatLng(location.latitude, location.longitude)
                                Log.d("MapScreen", "Ubicación actualizada: ${location.latitude}, ${location.longitude}")
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17f))
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

        // Barra de búsqueda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(40.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buscar kiosco...", color = Color.Gray)
        }
    }
}
