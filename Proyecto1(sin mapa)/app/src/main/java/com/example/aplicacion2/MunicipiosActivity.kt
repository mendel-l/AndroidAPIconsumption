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

class MunicipiosActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_municipios)

        listView = findViewById(R.id.municipiosListView)

        val departamentoId = intent.getIntExtra("DEPARTAMENTO_ID", -1)
        if (departamentoId == -1) {
            Toast.makeText(this, "Error al obtener ID del departamento", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://elephant-project.com/SicopApiMonitoreo/api/DashboardMapa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getMunicipios(departamentoId)
        call.enqueue(object : Callback<List<Municipio>> {
            override fun onResponse(call: Call<List<Municipio>>, response: Response<List<Municipio>>) {
                if (response.isSuccessful && response.body() != null) {
                    val municipios = response.body()!!
                    val adapter = MunicipioAdapter(this@MunicipiosActivity, municipios)
                    listView.adapter = adapter

                    // Agregar el OnItemClickListener aquí
                    listView.setOnItemClickListener { _, _, position, _ ->
                        val selectedMunicipio = municipios[position]
                        val intent = Intent(this@MunicipiosActivity, ProyectosActivity::class.java).apply {
                            putExtra("DEPARTAMENTO_ID", selectedMunicipio.DepartamentoID)
                            putExtra("MUNICIPIO_ID", selectedMunicipio.CodMunicipio)
                        }
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(this@MunicipiosActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Municipio>>, t: Throwable) {
                Toast.makeText(this@MunicipiosActivity, "Fallo la petición", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface ApiService {
        @GET("GetInformacionMunicipios/2022/{departamentoId}")
        fun getMunicipios(@Path("departamentoId") departamentoId: Int): Call<List<Municipio>>
    }

    data class Municipio(
        val MunicipioID: Int,
        val MunicipioNombre: String,
        val NoProyectos: Int,
        val PorcentajeAvance: Double,
        val DepartamentoID: Int,
        val CodMunicipio: Int
    )
}
