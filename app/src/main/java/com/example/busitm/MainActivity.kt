package com.example.busitm

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.busitm.databinding.ActivityMainBinding
import com.example.busitm.utils.*

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.btnUsuario.setOnClickListener { startMapActivity() }
        bind.btnChofer.setOnClickListener { startLoginActivity() }
        bind.btnReview.setOnClickListener { startReviewActivity() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GPS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMapActivity()
            } else {
                Toast.makeText(this, "Para usar esta app es necesario habilitar el permiso de GPS", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startMapActivity() {
        if (isGpsPermissionGranted(this, GPS_PERMISSION)) {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(MAIN, "Mi ubicación")
            startActivity(intent)
        } else {
            requestGpsPermission(this, GPS_PERMISSION, GPS_PERMISSION_CODE)
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun startReviewActivity() {
        val intent = Intent(this, ReviewActivity::class.java)
        startActivity(intent)
    }
}