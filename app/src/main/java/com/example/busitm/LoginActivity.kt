package com.example.busitm

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.busitm.databinding.ActivityLoginBinding
import com.example.busitm.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding
    private lateinit var BD: FirebaseFirestore
    private lateinit var user: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.btnIniciar.setOnClickListener { loginVerification() }
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
                Toast.makeText(this, "Para poder iniciar es necesario habilitar el permiso de GPS", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun loginVerification() {
        user = bind.edtNombre.text.toString()
        val password = bind.edtContra.text.toString()
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Por favor, llene los dos campos para poder iniciar", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    BD = Firebase.firestore
                    startMapActivity()
                } else
                    mostrarError(it.exception.toString())
            }
        }
    }
    private fun mostrarError(error: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Se ha producido un error de login.")
        builder.setMessage(error)
        builder.setPositiveButton("OK", null)
        val cuadroDialogo = builder.create()
        cuadroDialogo.show()
    }
    private fun startMapActivity() {
        if (isGpsPermissionGranted(this, GPS_PERMISSION))
            obtenerDatosChofer()
         else
            requestGpsPermission(this, GPS_PERMISSION, GPS_PERMISSION_CODE)
    }
    private fun obtenerDatosChofer() {
        val listaDatos = mutableListOf<String?>()
        BD.collection(COLLECTION)
            .document(user)
            .get()
            .addOnCompleteListener {
                val datos = it.result
                for (campo in CAMPOS)
                    listaDatos.add(datos?.get(campo) as String?)
            }
            .addOnCompleteListener {
                val usuario = listaDatos[0]!!
                RTDB.child(usuario).get().addOnSuccessListener {
                    if (it.exists())
                        mostrarError("Este usuario ya tiene una sesión iniciada. Por favor " +
                                "cierre sesión en su otro dispositivo antes de inciar aquí.")
                    else
                        iniciarMapIntent(listaDatos)
                }
            }
    }

    private fun iniciarMapIntent(datosCapturados: MutableList<String?>) {
        if (intent.extras == null) {
            val intent = Intent(this, MapActivity::class.java).apply {
                for ((i, dato) in datosCapturados.withIndex())
                    putExtra(LISTA_IDS[i], dato)
            }
            startActivity(intent)
        } else {
            val intent = Intent(this, ReviewActivity::class.java).apply {
                putExtra("CALLER", LOGIN)
                for ((i, dato) in datosCapturados.withIndex())
                    putExtra(LISTA_IDS[i], dato)
            }
            startActivity(intent)
        }
    }
}