package com.example.listview.AdaptadorPersonalizado

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.listview.Modelos.Municipio
import com.example.listview.R

class MunicipioAdapter(private val context: Context, private val municipios: List<Municipio>) : BaseAdapter() {

    override fun getCount(): Int = municipios.size

    override fun getItem(position: Int): Any = municipios[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.list_item_municipio, parent, false)

        val municipio = getItem(position) as Municipio

        val tvDepartamento = view.findViewById<TextView>(R.id.tvDepartamento)
        val tvMunicipio = view.findViewById<TextView>(R.id.tvMunicipio)
        val tvCodigoMunicipio = view.findViewById<TextView>(R.id.tvCodigo_municipio)
        val tvProyectos = view.findViewById<TextView>(R.id.tvProyectos)
        val tvAvance = view.findViewById<TextView>(R.id.tvAvance)
        val colorIndicator = view.findViewById<View>(R.id.colorIndicator)

        tvDepartamento.text = "Departamento: "+municipio.DepartamentoNombre
        tvMunicipio.text = "Municipio: "+municipio.MunicipioNombre
        tvCodigoMunicipio.text = "Codigo municpio: "+municipio.CodMunicipio.toString()
        tvProyectos.text = "No. de proyectos: ${municipio.NoProyectos}"
        tvAvance.text = "Avance: ${(municipio.PorcentajeAvance * 100).toInt()}%"

        // Cambiar el color del texto y del indicador de color según el valor de PorcentajeAvance
        val avance = (municipio.PorcentajeAvance * 100).toInt()

        styleTextView(context, tvAvance, avance)
        styleColorIndicator(context, colorIndicator, avance)

        return view
    }

    private fun styleTextView(context: Context, textView: TextView, avance: Int) {
        // Define el color del texto basado en el valor de avance
        val textColor = when {
            avance <= 50 -> ContextCompat.getColor(context, android.R.color.holo_red_light)
            avance <= 80 -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
            else -> ContextCompat.getColor(context, android.R.color.holo_green_light)
        }

        // Aplica el color del texto
        textView.setTextColor(textColor)
    }

    private fun styleColorIndicator(context: Context, view: View, avance: Int) {
        // Define el color del indicador de color basado en el valor de avance
        val color = when {
            avance <= 50 -> ContextCompat.getColor(context, android.R.color.holo_red_light)
            avance <= 80 -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
            else -> ContextCompat.getColor(context, android.R.color.holo_green_light)
        }

        // Aplica el color de fondo del indicador de color
        view.setBackgroundColor(color)
    }
}
