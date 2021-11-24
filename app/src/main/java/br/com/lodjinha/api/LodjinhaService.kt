package br.com.lodjinha.api

import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.models.GetProdutosCategoriaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LodjinhaService {

    @GET("banner")
    suspend fun getBanner(): Response<GetBannerResponse>

    @GET("categoria")
    suspend fun getCategoria(): Response<GetCategoriaResponse>

    @GET("produto/maisvendidos")
    suspend fun getMaisVendidos(): Response<GetMaisVendidosResponse>

    @GET("produto")
    suspend fun getProdutosCategoria(@Query("categoriaId") id: Int): Response<GetProdutosCategoriaResponse>
}