package com.example.aplicacion2

import retrofit2.Call
import retrofit2.http.GET

data class Departamento(
    val DepartamentoID: Int,
    val DepartamentoNombre: String,
    val NoProyectos: Int,
    val PorcentajeAvance: Double
)

interface ApiService {
    @GET("getDepartamentos")
    fun getDepartamentos(): Call<List<Departamento>>
}
