package com.tzeench.treeprotectormobile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

actual class LocationProvider actual constructor() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    constructor(context: Context) : this() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    actual fun fetchLocation(onResult: (String) -> Unit) {
        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                onResult("$lat, $long")
            } else {
                onResult("Location not available")
            }
        }

        task.addOnFailureListener {
            onResult("Error fetching location")
        }
    }
}