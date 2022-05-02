package com.example.busitm.utils

import com.google.firebase.database.FirebaseDatabase

const val GPS_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
const val GPS_PERMISSION_CODE = 100
const val MIN_UPDATE_TIME : Long = 2000 //ms
const val MIN_DISTANCE = 5F//meters
const val MAIN = "main"
const val LOGIN = "login"
const val COLLECTION_CHOFERES = "choferes"
const val COLLECTION_REVIEW = "reseñas"
const val REFERENCE = "choferesConectados"
const val LOGIN_NOMB = "login_nomb"
const val LOGIN_APE = "login_ape"
const val LOGIN_COD_RUTA = "login_cod_ruta"
const val LOGIN_NOMB_RUTA = "login_nomb_ruta"
val RTDB = FirebaseDatabase.getInstance().reference.child(REFERENCE)
val LISTA_IDS = listOf(LOGIN_NOMB, LOGIN_APE, LOGIN_COD_RUTA, LOGIN_NOMB_RUTA)
val CAMPOS = listOf("nombre", "apellido", "codigo_ruta", "nombre_ruta")
