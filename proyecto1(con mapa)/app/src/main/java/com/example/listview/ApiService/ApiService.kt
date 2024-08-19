package com.example.listview.ApiService

import com.example.listview.Modelos.Departamento
import com.example.listview.Modelos.Municipio
import com.example.listview.Modelos.Proyectos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceDepartamento {
    @GET("SicopApiMonitoreo/api/DashboardMapa/GetInformacionDepartamentos/2022")
    fun getDepartamentos(): Call<List<Departamento>>
}

interface ApiServiceMunicipio {
    @GET("SicopApiMonitoreo/api/DashboardMapa/GetInformacionMunicipios/2022/{departamentoID}")
    fun getMunicipios(@Path("departamentoID") departamentoID: Int): Call<List<Municipio>>
}
//https://apps.covial.gob.gt/sitiopublicomovilrv/Consultas?anio=2022&depto=900&muni=901&proy=cpe-019
interface ApiServiceProyecto {
    @GET("SicopApiMonitoreo/api/DashboardMapa/GetInformacionMunicipiosProyectos/2022/{departamentoID}/{municipioID}")
    fun getProyectos(
        @Path("departamentoID") departamentoID: Int,
        @Path("municipioID") municipioID: Int
    ): Call<List<Proyectos>>
}

