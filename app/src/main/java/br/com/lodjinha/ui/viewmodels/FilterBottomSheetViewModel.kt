package br.com.lodjinha.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.lodjinha.models.GetProdutosCategoriaResponse

class FilterBottomSheetViewModel: ViewModel() {

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