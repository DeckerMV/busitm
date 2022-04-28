package com.example.busitm

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.busitm.databinding.ActivityReviewBinding
import com.example.busitm.utils.LOGIN
import com.example.busitm.utils.MAIN
import org.w3c.dom.Text

class ReviewActivity : AppCompatActivity() {
    private lateinit var bind: ActivityReviewBinding
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
                var respuestas: Array<String?>? = null
                respuestas = if (pregunta!!.startsWith("8"))
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
    }
}