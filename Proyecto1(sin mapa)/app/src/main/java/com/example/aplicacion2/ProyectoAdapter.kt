package com.example.aplicacion2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProyectoAdapter(context: Context, private val proyectos: List<ProyectosActivity.Proyecto>) :
    ArrayAdapter<ProyectosActivity.Proyecto>(context, 0, proyectos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_proyecto, parent, false)

        val proyecto = proyectos[position]

        val codigoTextView = view.findViewById<TextView>(R.id.proyectoCodigo)
        val descripcionTextView = view.findViewById<TextView>(R.id.proyectoDescripcion)

        // Configura los textos
        codigoTextView.text = proyecto.ProyectoCodigo
        descripcionTextView.text = proyecto.ProyectoDescripcion

        return view
    }
}
