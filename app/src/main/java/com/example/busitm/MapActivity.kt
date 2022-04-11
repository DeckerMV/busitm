package com.example.busitm

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.busitm.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private var isIntentFromMain: Boolean? = null
    private lateinit var connectedChofer: String
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
        isIntentFromMain = intent.extras!!.get(MAIN) != null
        RTDB = FirebaseDatabase.getInstance().reference.child(REFERENCE)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        markerText = getIntentString()
        getLocationUpdates()
    }

    private fun getIntentString(): String {
        return when (isIntentFromMain) {
            true -> intent.extras!!.getString(MAIN)!!
            false -> {
                connectedChofer = intent.extras!!.getString(LOGIN_NOMB)!!
                intent.extras!!.getString(LOGIN_COD_RUTA)!!
            }
            else -> "NULL"
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
            .setMessage("¿Está seguro que desea cerrar sesión?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                if (isIntentFromMain == false)
                    removeChofer()
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val cuadroDialogo = builder.create()
        cuadroDialogo.show()
    }

    private fun removeChofer() {
        RTDB.child(connectedChofer).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(this, "Sesión cerrada...", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
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
        val latitude = loc.latitude
        val longitude = loc.longitude
        if (isIntentFromMain == false) {
            val surname = intent.extras!!.get(LOGIN_APE).toString()
            val cod_route = intent.extras!!.get(LOGIN_COD_RUTA).toString()
            val name_route = intent.extras!!.get(LOGIN_NOMB_RUTA).toString()
            val choferConectado = Chofer(
                connectedChofer, surname, cod_route, name_route, latitude, longitude)
            RTDB.child(connectedChofer).setValue(choferConectado)
        }
        relocateMarker(latitude, longitude)
    }

    private fun relocateMarker(latitude: Double, longitude: Double) {
        val newPosition = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLng(newPosition))
        theMarker.position = newPosition
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
                    val chofer = snapshot.child(connectedChofer).getValue(Chofer::class.java)
                    val latitude = chofer!!.latitud_actual!!
                    val longitude = chofer.longitud_actual!!
                    relocateMarker(latitude, longitude)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TEST", error.message)
            }
        })
    }


}