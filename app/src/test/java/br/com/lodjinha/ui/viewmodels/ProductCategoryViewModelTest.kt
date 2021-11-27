package br.com.lodjinha.ui.viewmodels

import br.com.lodjinha.models.GetProdutosCategoriaResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ProductCategoryViewModelTest {

    private lateinit var viewModel: FilterBottomSheetViewModel
    private var originalList: List<GetProdutosCategoriaResponse.ProdutoResponse>? = null

    @Before
    fun setUp() {
        viewModel = FilterBottomSheetViewModel()
        originalList = listProducts();
    }

    @Test
    fun `vailida ordenação dos produtos de forma ascendente`() {
        var processedList = viewModel.sortList(0 , originalList)

        assertThat(processedList?.get(0)?.descricao.equals("BMW M8")).isTrue()
        assertThat(processedList?.get(processedList.size -1)?.descricao.equals("Volkswagen Gol")).isTrue()
    }

    @Test
    fun `vailida ordenação dos produtos de forma decrescente`() {
        var processedList = viewModel.sortList(1 , originalList)

        assertThat(processedList?.get(0)?.descricao.equals("Volkswagen Gol")).isTrue()
        assertThat(processedList?.get(processedList.size -1)?.descricao.equals("BMW M8")).isTrue()
    }

    @Test
    fun `vailida ordenação original dos produtos`() {
        var processedList = viewModel.sortList(2 , originalList)

        assertThat(originalList).containsExactlyElementsIn(processedList).inOrder()
        assertThat(originalList?.get(0)?.descricao.equals(processedList?.get(0)?.descricao)).isTrue()
    }

    private fun listProducts (): ArrayList<GetProdutosCategoriaResponse.ProdutoResponse>? {
        val products = ArrayList<GetProdutosCategoriaResponse.ProdutoResponse>()
        products.add(getProduct(1,"Fiat Uno"))
        products.add(getProduct(2,"BMW M8"))
        products.add(getProduct(3,"Honda Civic"))
        products.add(getProduct(4,"Volkswagen Gol"))
        products.add(getProduct(5,"Toyota Corolla"))
        products.add(getProduct(6,"Ford Fiesta"))
        return products
    }

    private fun getProduct(id: Int, description: String): GetProdutosCategoriaResponse.ProdutoResponse {
        var categoria: GetProdutosCategoriaResponse.ProdutoResponse.Categoria = mockk()
        return GetProdutosCategoriaResponse.ProdutoResponse(
            id = id,
            descricao = description,
            categoria = categoria,
            nome = "String",
            precoDe = 0,
            precoPor = 0.0,
            urlImagem = "String"
        )
    }
}
