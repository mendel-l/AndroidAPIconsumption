package com.example.aplicacion2

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class ProyectosActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proyectos)

        listView = findViewById(R.id.proyectosListView)

        val municipioId = intent.getIntExtra("MUNICIPIO_ID", -1)

        if (municipioId == -1) {
            Toast.makeText(this, "Error al obtener datos del municipio", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://elephant-project.com/SicopApiMonitoreo/api/DashboardMapa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProyectos(2022, 100, municipioId) // Ajusta los valores según tu API
        call.enqueue(object : Callback<List<Proyecto>> {
            override fun onResponse(call: Call<List<Proyecto>>, response: Response<List<Proyecto>>) {
                if (response.isSuccessful && response.body() != null) {
                    val proyectos = response.body()!!
                    val adapter = ProyectoAdapter(this@ProyectosActivity, proyectos)
                    listView.adapter = adapter

                    // Configurar el OnItemClickListener
                    listView.setOnItemClickListener { parent, view, position, id ->
                        val selectedProyecto = proyectos[position]

                        val intent = Intent(this@ProyectosActivity, WebViewActivity::class.java).apply {
                            putExtra("anio", "2022") // Puedes ajustar estos valores según sea necesario
                            putExtra("depto", "100") // Asegúrate de ajustar este valor si es dinámico
                            putExtra("muni", municipioId.toString())
                            putExtra("proy", selectedProyecto.ProyectoCodigo)
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@ProyectosActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Proyecto>>, t: Throwable) {
                Toast.makeText(this@ProyectosActivity, "Fallo la petición", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface ApiService {
        @GET("GetInformacionMunicipiosProyectos/{anioId}/{departamentoId}/{municipioId}")
        fun getProyectos(
            @Path("anioId") anioId: Int,
            @Path("departamentoId") departamentoId: Int,
            @Path("municipioId") municipioId: Int
        ): Call<List<Proyecto>>
    }

    data class Proyecto(
        val ProyectoCodigo: String,
        val ProyectoDescripcion: String
    )
}
