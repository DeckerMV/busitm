package com.example.busitm

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.busitm.utils.GpsLocator
import com.example.busitm.utils.LOGIN_COD_RUTA
import com.example.busitm.utils.MAIN
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var markerText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        markerText = getIntentString()
    }

    private fun getIntentString(): String {
        val intent = this.intent //obtenemos el intent (u origen del activity) que lanzó MapActivity
        if (intent.extras!!.get(MAIN) != null)
            return intent.extras!!.getString(MAIN)!!
        else {
            return intent.extras!!.getString(LOGIN_COD_RUTA)!!
        }

    }

    override fun onMapReady(gm: GoogleMap) {
        map = gm
        GpsLocator.initializeLocation(this, getSystemService(Context.LOCATION_SERVICE))
        if (GpsLocator.currentLocation.equals(null)) {
            Toast.makeText(this, "Error al localizar la ubicación...", Toast.LENGTH_SHORT).show()
        } else {
            val currentLocation = GpsLocator.currentLocation
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title(markerText))
            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            map.setMaxZoomPreference(20f)
            map.setMinZoomPreference(15f)
        }
    }
}