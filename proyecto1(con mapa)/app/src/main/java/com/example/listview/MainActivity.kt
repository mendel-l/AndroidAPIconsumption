package com.example.listview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listview.ApiService.ApiClient
import com.example.listview.ApiService.ApiServiceDepartamento
import com.example.listview.Modelos.Departamento
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import android.location.Geocoder
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val markers = mutableListOf<Marker>()
    private lateinit var geocoder: Geocoder

    // Definir los límites de Guatemala
    private val guatemalaBounds = LatLngBounds(
        LatLng(13.393, -92.227), // Suroeste (Latitud, Longitud)
        LatLng(17.822, -88.199)  // Noreste (Latitud, Longitud)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geocoder = Geocoder(this, Locale.getDefault())

        // Configurar el mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Llamar a la API para obtener los departamentos
        val apiService = ApiClient.retrofit.create(ApiServiceDepartamento::class.java)
        apiService.getDepartamentos().enqueue(object : Callback<List<Departamento>> {
            override fun onResponse(call: Call<List<Departamento>>, response: Response<List<Departamento>>) {
                if (response.isSuccessful) {
                    val departamentos = response.body() ?: emptyList()
                    addMarkers(departamentos)
                } else {
                    Toast.makeText(this@MainActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Departamento>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Configurar clics en los marcadores
        mMap.setOnMarkerClickListener { marker ->
            val departamento = marker.tag as? Departamento
            departamento?.let {
                val intent = Intent(this@MainActivity, Municipios::class.java)
                intent.putExtra("DEPARTAMENTO_ID", it.DepartamentoID)
                Toast.makeText(this@MainActivity, "Departamento id: ${it.DepartamentoID}", Toast.LENGTH_SHORT).show()
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

            // Verificar si la ubicación está dentro de los límites de Guatemala
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
    }
}
