package br.com.lodjinha.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.models.GetProdutosCategoriaResponse
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.ui.viewstates.HomeData
import br.com.lodjinha.ui.viewstates.HomeViewState
import br.com.lodjinha.ui.viewstates.ProductCategoryViewState
import br.com.lodjinha.ui.viewstates.ProductsCategoryData
import br.com.lodjinha.utils.ResponseWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class ProductCategoryViewModel(
    private val repository: LodjinhaRepository
) : ViewModel() {

    private val _productCategoryDataLiveData = MutableLiveData<ProductCategoryViewState>()
    val productCategoryDataLiveData: LiveData<ProductCategoryViewState> get() = _productCategoryDataLiveData

    fun getProductsByCategoryData(id: Int) = viewModelScope.launch {
            _productCategoryDataLiveData.postValue(
                ProductCategoryViewState(
                loading = true
            )
        )

        val productsCategoryResponse = async {repository.getProdutosCategoria(id)}

        val productCategoryState = handleProductCategoryResponses(
            productsCategoryResponse.await(),
        )

        _productCategoryDataLiveData.postValue(productCategoryState)
    }

    private fun handleProductCategoryResponses(
        productsCategoryResponse: ResponseWrapper<GetProdutosCategoriaResponse>,
    ): ProductCategoryViewState {
        var productsCategoryData: List<GetProdutosCategoriaResponse.ProdutoResponse>? = null

        when (productsCategoryResponse) {
            is ResponseWrapper.Success -> {
                productsCategoryData = productsCategoryResponse.result.data
            }
        }

        if (productsCategoryData != null) {

            return ProductCategoryViewState(
                loading = false,
                error = false,
                data = ProductsCategoryData(
                    productsCategoryData = productsCategoryData,
                )
            )
        }

        return ProductCategoryViewState(
            loading = false,
            error = true,
            data = null
        )
    }

    fun sortList(filterItemId: Int, products: List<GetProdutosCategoriaResponse.ProdutoResponse>?)
        : List<GetProdutosCategoriaResponse.ProdutoResponse>? {

        if (filterItemId == 0) {
            return products?.sortedBy { it.descricao }
        }
        if (filterItemId == 1) {
            return products?.sortedByDescending { it.descricao }
        }
        return products
    }
}