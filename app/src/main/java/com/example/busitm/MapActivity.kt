package com.example.busitm

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.busitm.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var map: GoogleMap
    private lateinit var markerText: String
    private lateinit var RTDB: DatabaseReference
    private lateinit var locationManager: LocationManager
    private lateinit var theMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        RTDB = FirebaseDatabase.getInstance().reference.child(REFERENCE)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        getLocationUpdates()
        readChanges()
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
        val matamorosCoords = LatLng(25.865623572202768, -97.50533393880447)
        map = gm
        theMarker = map.addMarker(
            MarkerOptions()
                .position(matamorosCoords)
                .title(markerText))!!
            map.moveCamera(CameraUpdateFactory.newLatLng(matamorosCoords))
            map.setMaxZoomPreference(20f)
            map.setMinZoomPreference(15f)
    }

    override fun onLocationChanged(loc: Location) {
        val testChofer = Chofer(
            "Erick",
            "Martinez",
            "RUT-ERICK",
            "ErickRuta",
            loc.latitude,
            loc.longitude
        )
        RTDB.child(testChofer.nombre!!).setValue(testChofer)
    }

    private fun getLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, GPS_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_DISTANCE, this)
                }
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME, MIN_DISTANCE, this)
                }
                else -> {
                    Toast.makeText(this, "Por favor, habilite su red o GPS", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestGpsPermission(this, GPS_PERMISSION, GPS_PERMISSION_CODE)
        }
    }

    private fun readChanges() {
        RTDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val chofer = snapshot.child("Erick").getValue(Chofer::class.java)
                    val latitude = chofer!!.latitudActual!!
                    val longitude = chofer.longitudActual!!
                    val newPosition = LatLng(latitude, longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLng(newPosition))
                    theMarker.position = newPosition
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TEST", error.message)
            }
        })
    }

}