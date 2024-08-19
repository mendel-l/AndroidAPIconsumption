package com.example.aplicacion2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DepartamentoAdapter(context: Context, private val departamentos: List<Departamento>) :
    ArrayAdapter<Departamento>(context, 0, departamentos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val departamento = departamentos[position]

        val nombreTextView = view.findViewById<TextView>(R.id.departamentoNombre)
        val noProyectosTextView = view.findViewById<TextView>(R.id.noProyectos)
        val avanceTextView = view.findViewById<TextView>(R.id.porcentajeAvance)

        nombreTextView.text = departamento.DepartamentoNombre
        noProyectosTextView.text = "Número de Proyectos: ${departamento.NoProyectos}"
        avanceTextView.text = String.format("%.2f%%", departamento.PorcentajeAvance * 100)

        // Define color based on PorcentajeAvance
        val porcentaje = departamento.PorcentajeAvance * 100
        val color = when {
            porcentaje <= 50 -> Color.parseColor("#FFCDD2") // Rojo pastel
            porcentaje <= 80 -> Color.parseColor("#FFF9C4") // Amarillo pastel
            else -> Color.parseColor("#C8E6C9") // Verde pastel
        }

        avanceTextView.setBackgroundColor(color)

        return view
    }
}
