package br.com.lodjinha.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.repositories.LodjinhaRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: LodjinhaRepository
) : ViewModel() {

    private val _bannerLiveData = MutableLiveData<List<GetBannerResponse.Banner>>()
    val bannerLiveData: LiveData<List<GetBannerResponse.Banner>> get() = _bannerLiveData

    fun getBanner() = viewModelScope.launch {
        val response = repository.getBanner()

        if (response.isSuccessful) {
            response.body()?.let {
                _bannerLiveData.postValue(it.data)
            }
        }

    }

    private val _categoriesLiveData = MutableLiveData<List<GetCategoriaResponse.Categoria>>()
    val categoriesLiveData: LiveData<List<GetCategoriaResponse.Categoria>> get() = _categoriesLiveData

    fun getCategories() = viewModelScope.launch {
        val response = repository.getCategoria()

        if (response.isSuccessful) {
            response.body()?.let {
                _categoriesLiveData.postValue(it.data)
            }
        }
    }

    private val _maisVendidosLiveData = MutableLiveData<List<GetMaisVendidosResponse.ProdutoResponse>>()
    val maisVendidosLiveData: LiveData<List<GetMaisVendidosResponse.ProdutoResponse>> get() = _maisVendidosLiveData

    fun getMaisVendidos() = viewModelScope.launch {
        val response = repository.getMaisVendidos()

        if (response.isSuccessful) {
            response.body()?.let {
                _maisVendidosLiveData.postValue(it.data)
            }
        }
    }
}