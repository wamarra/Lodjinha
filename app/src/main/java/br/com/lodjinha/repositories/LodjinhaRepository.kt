package br.com.lodjinha.repositories

import br.com.lodjinha.api.LodjinhaService
import br.com.lodjinha.api.SafeApiCall
import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.utils.ResponseWrapper
import retrofit2.Response

class LodjinhaRepository(
    private val service: LodjinhaService
) {

    suspend fun getBanner(): ResponseWrapper<GetBannerResponse> {
        service.getBanner()?.let {
            if (it.body() != null) {
                return ResponseWrapper.Success(it.body()!!)
            }
        }
        return ResponseWrapper.Error("Erro ao carregar os banners")
    }

    suspend fun getCategoria(): ResponseWrapper<GetCategoriaResponse> {
        service.getCategoria()?.let {
            if (it.body() != null) {
                return ResponseWrapper.Success(it.body()!!)
            }
        }
        return ResponseWrapper.Error("Erro ao carregar as categorias")
    }

    suspend fun getMaisVendidos(): ResponseWrapper<GetMaisVendidosResponse> {
        service.getMaisVendidos()?.let {
            if (it.body() != null) {
                return ResponseWrapper.Success(it.body()!!)
            }
        }
        return ResponseWrapper.Error("Erro ao carregar os mais vendidos")
    }
}