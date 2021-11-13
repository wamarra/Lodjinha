package br.com.lodjinha.ui.viewstates

import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse

data class HomeViewState (
    var loading: Boolean = false,
    var error: Boolean = false,
    var data: HomeData? = null
)

data class HomeData(
    val bannerData: List<GetBannerResponse.Banner>?,
    val categoriesData: List<GetCategoriaResponse.Categoria>?,
    val maisVendidosData: List<GetMaisVendidosResponse.ProdutoResponse>?
)