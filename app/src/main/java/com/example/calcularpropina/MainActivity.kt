package com.example.calcularpropina

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var montoEditText: EditText
    private lateinit var personasEditText: EditText
    private lateinit var resultadoTextView: TextView
    private lateinit var calcularBtn: Button
    private lateinit var limpiarBtn: Button
    private lateinit var ivaSwitch: Switch
    private lateinit var radio1: RadioButton
    private lateinit var radio2: RadioButton
    private lateinit var radio3: RadioButton
    private lateinit var radio4: RadioButton
    private lateinit var propinaPersonalizadaEditText: EditText

    private val IVA = 0.16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias
        montoEditText = findViewById(R.id.editTextMonto)
        personasEditText = findViewById(R.id.editTextPersonas)
        resultadoTextView = findViewById(R.id.resul)
        calcularBtn = findViewById(R.id.calcular)
        limpiarBtn = findViewById(R.id.clean)
        ivaSwitch = findViewById(R.id.iva)
        radio1 = findViewById(R.id.radio1)
        radio2 = findViewById(R.id.radio2)
        radio3 = findViewById(R.id.radio3)
        radio4 = findViewById(R.id.radio4)
        propinaPersonalizadaEditText = findViewById(R.id.propinaPersonalizada)

        // Mostrar/ocultar campo personalizado
        radio4.setOnCheckedChangeListener { _, isChecked ->
            propinaPersonalizadaEditText.visibility = if (isChecked) {
                EditText.VISIBLE
            } else {
                EditText.GONE
            }
        }

        calcularBtn.setOnClickListener {
            calcularPropina()
        }

        limpiarBtn.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun calcularPropina() {
        val montoTexto = montoEditText.text.toString()
        val personasTexto = personasEditText.text.toString()

        if (montoTexto.isEmpty() || personasTexto.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoTexto.toDoubleOrNull()
        val personas = personasTexto.toIntOrNull()

        if (monto == null || monto <= 0 || personas == null || personas <= 0) {
            Toast.makeText(this, getString(R.string.error_valores_invalidos), Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener porcentaje de propina
        val porcentaje = when {
            radio1.isChecked -> 10.0
            radio2.isChecked -> 15.0
            radio3.isChecked -> 20.0
            radio4.isChecked -> {
                val texto = propinaPersonalizadaEditText.text.toString()
                texto.toDoubleOrNull() ?: 0.0
            }
            else -> 0.0
        }

        val propina = monto * (porcentaje / 100)
        val iva = if (ivaSwitch.isChecked) monto * IVA else 0.0
        val total = monto + propina + iva
        val porPersona = total / personas

        val resultado = """
            Propina: $${"%.2f".format(propina)}
            Total a pagar: $${"%.2f".format(total)}
            Por persona: $${"%.2f".format(porPersona)}
        """.trimIndent()

        resultadoTextView.text = resultado
    }

    private fun limpiarCampos() {
        montoEditText.setText("")
        personasEditText.setText("")
        resultadoTextView.text = ""
        ivaSwitch.isChecked = false
        radio1.isChecked = false
        radio2.isChecked = false
        radio3.isChecked = false
        radio4.isChecked = false
        propinaPersonalizadaEditText.setText("")
        propinaPersonalizadaEditText.visibility = EditText.GONE
    }
}
