package br.com.lodjinha.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.ui.viewstates.HomeData
import br.com.lodjinha.ui.viewstates.HomeViewState
import br.com.lodjinha.utils.ResponseWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.system.measureTimeMillis

class MainViewModel(
    private val repository: LodjinhaRepository
) : ViewModel() {

    private val _homeDataLiveData = MutableLiveData<HomeViewState>()
    val homeDataLiveData: LiveData<HomeViewState> get() = _homeDataLiveData

    fun getMainHomeData() = viewModelScope.launch {
        _homeDataLiveData.postValue(HomeViewState(
            loading = true
        ))

        val time1 = measureTimeMillis {
            val bannerResponse = async {repository.getBanner()}
            val categoriesResponse = async {repository.getCategoria()}
            val maisVendidosResponse = async {repository.getMaisVendidos()}

            val homeState = handleHomeResponses(
                bannerResponse.await(),
                categoriesResponse.await(),
                maisVendidosResponse.await()
            )

            _homeDataLiveData.postValue(homeState)
        }

        println("Tempo do request é? $time1 ms")
    }

    private fun handleHomeResponses(
        bannerResponse: ResponseWrapper<GetBannerResponse>,
        categoriesResponse: ResponseWrapper<GetCategoriaResponse>,
        maisVendidosResponse: ResponseWrapper<GetMaisVendidosResponse>
    ): HomeViewState {
        var bannerData: List<GetBannerResponse.Banner>? = null
        var categoriesData: List<GetCategoriaResponse.Categoria>? = null
        var maisVendidosData: List<GetMaisVendidosResponse.ProdutoResponse>? = null

        when (bannerResponse) {
            is ResponseWrapper.Success -> {
                bannerData = bannerResponse.result.data
            }
        }
        when (categoriesResponse) {
            is ResponseWrapper.Success -> {
                categoriesData = categoriesResponse.result.data
            }
        }
        when (maisVendidosResponse) {
            is ResponseWrapper.Success -> {
                maisVendidosData = maisVendidosResponse.result.data
            }
        }

        if (bannerData != null &&
            categoriesData != null &&
            maisVendidosData != null) {

            return HomeViewState(
                loading = false,
                error = false,
                data = HomeData(
                    bannerData = bannerData,
                    categoriesData = categoriesData,
                    maisVendidosData = maisVendidosData
                )
            )
        }

        return HomeViewState(
            loading = false,
            error = true,
            data = null
        )
    }
}