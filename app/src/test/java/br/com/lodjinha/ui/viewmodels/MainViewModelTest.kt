package br.com.lodjinha.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.lodjinha.MainCoroutineRule
import br.com.lodjinha.getOrAwaitValueTest
import br.com.lodjinha.models.GetBannerResponse
import br.com.lodjinha.models.GetCategoriaResponse
import br.com.lodjinha.models.GetMaisVendidosResponse
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.utils.ResponseWrapper
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: MainViewModel

    val repository: LodjinhaRepository = mockk()

    @Before
    fun setUp() {
        viewModel = MainViewModel(repository)
    }


    @Test
    fun `validar que o mainHomestate eh loading ao iniciar o getMainHomeData`() {
        mainCoroutineRule.runBlockingTest {

            val result = viewModel.homeDataLiveData.getOrAwaitValueTest (
                positionOfValueToBeCatch = 1
            ) {
                coEvery { repository.getBanner() } returns ResponseWrapper.Success(GetBannerResponse(arrayListOf()))
                coEvery { repository.getCategoria() } returns ResponseWrapper.Success(GetCategoriaResponse(arrayListOf()))
                coEvery { repository.getMaisVendidos() } returns ResponseWrapper.Success(GetMaisVendidosResponse(arrayListOf())
                )

                viewModel.getMainHomeData()
            }

            assertThat(result[0].loading).isTrue()
            assertThat(result[1].loading).isFalse()

        }
    }

    @Test
    fun `validar que o mainState retorne com Erro caso um dos endpoints retorne com erro`() {
        mainCoroutineRule.runBlockingTest {

            val result = viewModel.homeDataLiveData.getOrAwaitValueTest (
                positionOfValueToBeCatch = 1
            ) {
                coEvery { repository.getBanner() } returns ResponseWrapper.Success(GetBannerResponse(arrayListOf()))
                coEvery { repository.getCategoria() } returns ResponseWrapper.Error("Erro o buscar as categorias")
                coEvery { repository.getMaisVendidos() } returns ResponseWrapper.Success(GetMaisVendidosResponse(arrayListOf())
                )

                viewModel.getMainHomeData()
            }

            assertThat(result[0].loading).isTrue()
            assertThat(result[1].error).isTrue()

        }
    }

    @Test
    fun `Validar que o data State nao eh null quando os 3 requests vierem com success`() {
        mainCoroutineRule.runBlockingTest {

            val result = viewModel.homeDataLiveData.getOrAwaitValueTest (
                positionOfValueToBeCatch = 1
            ) {
                coEvery { repository.getBanner() } returns ResponseWrapper.Success(GetBannerResponse(arrayListOf()))
                coEvery { repository.getCategoria() } returns ResponseWrapper.Success(GetCategoriaResponse(arrayListOf()))
                coEvery { repository.getMaisVendidos() } returns ResponseWrapper.Success(GetMaisVendidosResponse(arrayListOf())
                )

                viewModel.getMainHomeData()
            }

            assertThat(result[0].loading).isTrue()
            assertThat(result[1].data).isNotNull()

        }
    }
}