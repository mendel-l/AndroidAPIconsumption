package com.example.aplicacion2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.location.Geocoder
import android.widget.Button
import java.io.IOException
import java.util.Locale

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val markers = mutableListOf<Marker>()
    private lateinit var geocoder: Geocoder

    private val guatemalaBounds = LatLngBounds(
        LatLng(13.393, -92.227), // Suroeste (Latitud, Longitud)
        LatLng(17.822, -88.199)  // Noreste (Latitud, Longitud)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geocoder = Geocoder(this, Locale.getDefault())

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val apiService = Retrofit.Builder()
            .baseUrl("https://elephant-project.com/SicopApiMonitoreo/api/DashboardMapa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Llamada al método getDepartamentos de la interfaz ApiService
        apiService.getDepartamentos().enqueue(object : Callback<List<Departamento>> {
            override fun onResponse(call: Call<List<Departamento>>, response: Response<List<Departamento>>) {
                if (response.isSuccessful) {
                    val departamentos = response.body() ?: emptyList()
                    addMarkers(departamentos)
                } else {
                    Toast.makeText(this@MapaActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Departamento>>, t: Throwable) {
                Toast.makeText(this@MapaActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener { marker ->
            val departamento = marker.tag as? Departamento
            departamento?.let {
                val intent = Intent(this@MapaActivity, MunicipiosActivity::class.java)
                intent.putExtra("DEPARTAMENTO_ID", it.DepartamentoID)
                startActivity(intent)
            }
            true
        }
    }

    private fun addMarkers(departamentos: List<Departamento>) {
        val boundsBuilder = LatLngBounds.Builder()
        for (departamento in departamentos) {
            val addressList = try {
                geocoder.getFromLocationName(departamento.DepartamentoNombre, 1)
            } catch (e: IOException) {
                e.printStackTrace()
                continue
            }

            if (addressList.isNullOrEmpty()) {
                continue
            }

            val address = addressList[0]
            val location = LatLng(address.latitude, address.longitude)

            if (guatemalaBounds.contains(location)) {
                val marker = mMap.addMarker(MarkerOptions().position(location).title(departamento.DepartamentoNombre))
                marker?.let {
                    it.tag = departamento
                    markers.add(it)
                    boundsBuilder.include(location)
                }
            }
        }

        if (markers.isNotEmpty()) {
            val bounds = boundsBuilder.build()
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
            mMap.moveCamera(cameraUpdate)
        }

        val mapButton = findViewById<Button>(R.id.mapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }
    }
}
