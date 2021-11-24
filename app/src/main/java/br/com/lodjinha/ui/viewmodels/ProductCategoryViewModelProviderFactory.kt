package br.com.lodjinha.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.lodjinha.repositories.LodjinhaRepository

class ProductCategoryViewModelProviderFactory (
    private val repository: LodjinhaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductCategoryViewModel(repository) as T
    }
}