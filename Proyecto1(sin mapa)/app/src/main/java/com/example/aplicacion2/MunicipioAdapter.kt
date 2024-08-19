package com.example.aplicacion2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MunicipioAdapter(context: Context, private val municipios: List<MunicipiosActivity.Municipio>) :
    ArrayAdapter<MunicipiosActivity.Municipio>(context, 0, municipios) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val municipio = municipios[position]

        val nombreTextView = view.findViewById<TextView>(R.id.departamentoNombre)
        val noProyectosTextView = view.findViewById<TextView>(R.id.noProyectos)
        val avanceTextView = view.findViewById<TextView>(R.id.porcentajeAvance)

        nombreTextView.text = municipio.MunicipioNombre
        noProyectosTextView.text = "Número de Proyectos: ${municipio.NoProyectos}"
        avanceTextView.text = String.format("%.2f%%", municipio.PorcentajeAvance * 100)

        // Define color based on PorcentajeAvance
        val porcentaje = municipio.PorcentajeAvance * 100
        val color = when {
            porcentaje <= 50 -> Color.parseColor("#FFCDD2") // Rojo pastel
            porcentaje <= 80 -> Color.parseColor("#FFF9C4") // Amarillo pastel
            else -> Color.parseColor("#C8E6C9") // Verde pastel
        }

        avanceTextView.setBackgroundColor(color)

        return view
    }
}
