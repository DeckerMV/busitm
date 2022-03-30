package com.example.busitm.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

fun requestGpsPermission(activity: Activity?, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
}

fun isGpsPermissionGranted(context: AppCompatActivity, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}