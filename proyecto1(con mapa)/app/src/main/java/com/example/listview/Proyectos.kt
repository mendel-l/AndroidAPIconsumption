package com.example.listview

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.listview.AdaptadorPersonalizado.MunicipioAdapter
import com.example.listview.AdaptadorPersonalizado.ProyectosAdapter
import com.example.listview.ApiService.ApiClient
import com.example.listview.ApiService.ApiServiceProyecto
import com.example.listview.Modelos.Proyectos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent

class Proyectos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_proyectos)

        val departamentoID = intent.getIntExtra("DEPARTAMENTO_ID", -1)
        val municipioID = intent.getIntExtra("MUNICIPIO_ID", -1)
        val listView: ListView = findViewById(R.id.listview)

        if (departamentoID != -1 && municipioID != -1) {
            val apiService = ApiClient.retrofit.create(ApiServiceProyecto::class.java)
            apiService.getProyectos(departamentoID, municipioID).enqueue(object : Callback<List<Proyectos>> {
                override fun onResponse(call: Call<List<Proyectos>>, response: Response<List<Proyectos>>) {
                    if (response.isSuccessful) {
                        val proyectos = response.body() ?: emptyList()
                        val adapter = ProyectosAdapter(this@Proyectos, proyectos)
                        listView.adapter = adapter
                        listView.setOnItemClickListener { _, _, position, _ ->
                            val proyecto = proyectos[position]
                            val intent = Intent(this@Proyectos, webviewCOVIAL::class.java)
                            intent.putExtra("PROYECTO_CODIGO", proyecto.ProyectoCodigo)
                            Toast.makeText(this@Proyectos, "Codigo de proyecto: ${proyecto.ProyectoCodigo}", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(this@Proyectos, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Proyectos>>, t: Throwable) {
                    Toast.makeText(this@Proyectos, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID del departamento o municipio inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
