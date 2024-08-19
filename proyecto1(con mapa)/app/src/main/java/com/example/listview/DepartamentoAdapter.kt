package com.example.listview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class DepartamentoAdapter(private val context: Context, private val departamentos: List<Departamento>) : BaseAdapter() {

    override fun getCount(): Int = departamentos.size

    override fun getItem(position: Int): Any = departamentos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

        val departamento = getItem(position) as Departamento

        val tvNombre = view.findViewById<TextView>(R.id.tvNombre)
        val tvProyectos = view.findViewById<TextView>(R.id.tvProyectos)
        val tvAvance = view.findViewById<TextView>(R.id.tvAvance)

        tvNombre.text = departamento.DepartamentoNombre
        tvProyectos.text = "${departamento.NoProyectos} proyectos"
        tvAvance.text = "Avance: ${departamento.PorcentajeAvance * 100}%"

        // Cambiar el color de fondo según el valor de PorcentajeAvance
        val avance = departamento.PorcentajeAvance * 100

        val backgroundColor = when {
            avance <= 50 -> ContextCompat.getColor(context, android.R.color.holo_red_light)
            avance <= 80 -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
            else -> ContextCompat.getColor(context, android.R.color.holo_green_light)
        }

        view.setBackgroundColor(backgroundColor)

        return view
    }
}
