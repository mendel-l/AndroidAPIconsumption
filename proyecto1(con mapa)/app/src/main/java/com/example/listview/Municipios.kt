package com.example.listview

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listview.ApiService.ApiClient
import com.example.listview.ApiService.ApiServiceMunicipio
import com.example.listview.AdaptadorPersonalizado.MunicipioAdapter
import com.example.listview.Modelos.Municipio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent

class Municipios : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var municipios: List<Municipio>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_municipios)

        listView = findViewById(R.id.listview)

        val departamentoID = intent.getIntExtra("DEPARTAMENTO_ID", -1)

        if (departamentoID != -1) {
            val apiService = ApiClient.retrofit.create(ApiServiceMunicipio::class.java)
            apiService.getMunicipios(departamentoID).enqueue(object : Callback<List<Municipio>> {
                override fun onResponse(call: Call<List<Municipio>>, response: Response<List<Municipio>>) {
                    if (response.isSuccessful) {
                        municipios = response.body() ?: emptyList()
                        val adapter = MunicipioAdapter(this@Municipios, municipios)
                        listView.adapter = adapter
                        listView.setOnItemClickListener { _, _, position, _ ->
                            val selectedMunicipio = municipios[position]
                            val intent = Intent(this@Municipios, Proyectos::class.java).apply {
                                putExtra("DEPARTAMENTO_ID", departamentoID)
                                putExtra("MUNICIPIO_ID", selectedMunicipio.CodMunicipio)
                                Toast.makeText(this@Municipios, "Departamento id: ${departamentoID}\nMunicipio id: ${selectedMunicipio.CodMunicipio}", Toast.LENGTH_SHORT).show()
                            }
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@Municipios, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Municipio>>, t: Throwable) {
                    Toast.makeText(this@Municipios, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID del departamento inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
