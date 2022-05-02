package com.example.busitm

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.busitm.databinding.ActivityReviewBinding
import com.example.busitm.utils.COLLECTION_REVIEW
import com.example.busitm.utils.LOGIN
import com.example.busitm.utils.MAIN
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReviewActivity : AppCompatActivity() {
    private lateinit var bind: ActivityReviewBinding
    private lateinit var BD: FirebaseFirestore
    private lateinit var preguntasUsuario: Array<String?>
    private lateinit var preguntasChofer: Array<String?>
    private lateinit var respuestasNivel: Array<String?>
    private lateinit var respuestasSiNo: Array<String?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(bind.root)
        preguntasUsuario = resources.getStringArray(R.array.preguntasUsuario)
        preguntasChofer = resources.getStringArray(R.array.preguntasChofer)
        respuestasNivel = resources.getStringArray(R.array.respuestasNivel)
        respuestasSiNo = resources.getStringArray(R.array.respuestasSiNo)
        poblarLista()
    }

    private fun poblarLista() {
        if (intent.extras!!.get("CALLER") == MAIN) {
            for (pregunta in preguntasUsuario) {
                val tvPregunta = TextView(this)
                tvPregunta.text = pregunta
                tvPregunta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                tvPregunta.setTextColor(ContextCompat.getColor(this, R.color.teal_700))
                tvPregunta.setTypeface(null, Typeface.BOLD)
                bind.SVLL.addView(tvPregunta)
                var respuestas: Array<String?>? = null
                respuestas = if (pregunta!!.startsWith("5") || pregunta.startsWith("6"))
                    respuestasNivel
                else
                    respuestasSiNo
                val rg = RadioGroup(this)
                for (res in respuestas) {
                    val rbRes = RadioButton(this)
                    rbRes.text = res!!
                    rg.addView(rbRes)
                }
                bind.SVLL.addView(rg)
            }
        } else if (intent.extras!!.get("CALLER") == LOGIN) {
            for (pregunta in preguntasChofer) {
                val tvPregunta = TextView(this)
                tvPregunta.text = pregunta
                tvPregunta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                tvPregunta.setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                tvPregunta.setTypeface(null, Typeface.BOLD)
                bind.SVLL.addView(tvPregunta)
                var respuestas: Array<String?> = if (pregunta!!.startsWith("8"))
                    respuestasNivel
                else
                    respuestasSiNo
                val rg = RadioGroup(this)
                for (res in respuestas) {
                    val rbRes = RadioButton(this)
                    rbRes.text = res!!
                    rg.addView(rbRes)
                }
                bind.SVLL.addView(rg)
            }
        }
        val btnEnviar = Button(this)
        btnEnviar.text = "ENVIAR"
        bind.SVLL.addView(btnEnviar)
        btnEnviar.setOnClickListener { cargarRespuestas() }
    }

    private fun cargarRespuestas() {
        val respuestas = mutableListOf<String>()
        val childrenViews = bind.SVLL.children
        for (child in childrenViews) {
            if (child is RadioGroup) {
                if (child.checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Por favor, conteste todas las preguntas.",
                        Toast.LENGTH_SHORT).show()
                    respuestas.clear()
                    break
                } else {
                    val checked = findViewById<RadioButton>(child.checkedRadioButtonId)
                    respuestas.add(checked.text.toString())
                }
            }
        }
        if (respuestas.isEmpty()) {
            return
        } else {
            subirRespuestas(respuestas)
        }
    }

    private fun subirRespuestas(respuestas: List<String>) {
        BD = Firebase.firestore
        val review = hashMapOf<String, String>()
        for ((i, res) in respuestas.withIndex()) { review["${i + 1}"] = res }
        BD.collection(COLLECTION_REVIEW)
            .add(review)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Gracias por contestar!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.e("ERROR", it.message!!)
            }
    }
}