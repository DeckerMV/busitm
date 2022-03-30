package com.example.busitm.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener

object GpsLocator {

    lateinit var currentLocation: LatLng

    fun initializeLocation(context: Activity, locationService: Any) {
        val locationManager = locationService as LocationManager //casteando el ServicioGPS al manager de localización
        if (ActivityCompat.checkSelfPermission(context, GPS_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                currentLocation = LatLng(latitude, longitude)
            }
        } else {
            requestGpsPermission(context, GPS_PERMISSION, GPS_PERMISSION_CODE)
        }
    }

}