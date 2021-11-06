package br.com.lodjinha.api

import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import retrofit2.Response
import retrofit2.http.GET

interface LodjinhaService {

    @GET("banner")
    suspend fun getBanner(): Response<GetBannerResponse>

    @GET("categoria")
    suspend fun getCategoria(): Response<GetCategoriaResponse>

    @GET("produto/maisvendidos")
    suspend fun getMaisVendidos(): Response<GetMaisVendidosResponse>
}