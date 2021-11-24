package br.com.lodjinha.ui.viewstates

import br.com.lodjinha.models.GetProdutosCategoriaResponse

data class ProductCategoryViewState (
    var loading: Boolean = false,
    var error: Boolean = false,
    var data: ProductsCategoryData? = null
)

data class ProductsCategoryData(
    val productsCategoryData: List<GetProdutosCategoriaResponse.ProdutoResponse>?,
)