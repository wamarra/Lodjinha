package br.com.lodjinha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import br.com.lodjinha.R
import br.com.lodjinha.api.RetrofitInstance
import br.com.lodjinha.databinding.FragmentHomeBinding
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.ui.adapters.BannerAdapter
import br.com.lodjinha.ui.adapters.CategoriasAdapter
import br.com.lodjinha.ui.adapters.ProductsAdapter
import br.com.lodjinha.ui.viewmodels.MainViewModel
import br.com.lodjinha.ui.viewmodels.MainViewModelProviderFactory
import br.com.lodjinha.utils.toggleVisibilty
import coil.load
import coil.transform.CircleCropTransformation

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoriasAdapter: CategoriasAdapter
    private lateinit var maisVendidosAdapter: ProductsAdapter


    private val viewModel: MainViewModel by lazy {
        val repository = LodjinhaRepository(RetrofitInstance.apiService)
        val viewModelProviderFactory = MainViewModelProviderFactory(repository)
        ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBannerRv()

        setupCategoriaRv()

        setupMaisVendidosRv()

        setupObservers()

        viewModel.getMainHomeData()

//        viewModel.getCategories()
//
//        viewModel.getMaisVendidos()

    }

    private fun setupMaisVendidosRv() {
        maisVendidosAdapter = ProductsAdapter()
        binding.maisVendidosRv.adapter = maisVendidosAdapter
        maisVendidosAdapter.setOnItemClickListener { produtoResponse ->
            println(produtoResponse)

            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToProductViewFragment(
                    title = produtoResponse.nome,
                    productId = produtoResponse.id,
                    tvProductCategory = produtoResponse.categoria.descricao,
                    tvProdcutName = produtoResponse.nome,
                    tvProductPrice2 = produtoResponse.precoPor.toString(),
                    tvProductPriceFrom2 = produtoResponse.precoDe.toString(),
                    tvProductDescription = produtoResponse.descricao,
                    urlImagem = produtoResponse.urlImagem
                )
            )
        }
    }

    private fun setupCategoriaRv() {
        categoriasAdapter = CategoriasAdapter()
        binding.rvCategories.adapter = categoriasAdapter
        categoriasAdapter.setOnItemClickListener { categoria ->
            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToProductsListFragment(
                    title = categoria.descricao,
                    categoryId = categoria.id
                )
            )
        }
    }

    private fun setupObservers() {
        viewModel.homeDataLiveData.observe(viewLifecycleOwner) { viewState ->
            when {
                viewState.loading -> {
                    binding.mainLayout.toggleVisibilty(false)
                    binding.progress.toggleVisibilty(true)
                }
                viewState.error -> {
                    // TODO Criar placeholder caso de erro
                }
                viewState.data != null -> {
                    binding.progress.toggleVisibilty(false)
                    binding.mainLayout.toggleVisibilty(true)

                    if (viewState.data?.bannerData.isNullOrEmpty().not()) {
                        bannerAdapter.differ.submitList(viewState.data?.bannerData)
                    }

                    if (viewState.data?.categoriesData.isNullOrEmpty().not()) {
                        categoriasAdapter.differ.submitList(viewState.data?.categoriesData)
                    }

                    if (viewState.data?.maisVendidosData.isNullOrEmpty().not()) {
                        maisVendidosAdapter.differ.submitList(viewState.data?.maisVendidosData)
                    }
                }
            }
        }
    }

    // TODO SETUP OBSERVERS AND POPULATE RECYCLERVIEW

    private fun setupBannerRv() {
        bannerAdapter = BannerAdapter()
        binding.rvBanner.adapter = bannerAdapter
        PagerSnapHelper().attachToRecyclerView(binding.rvBanner)
        binding.indicator.attachToRecyclerView(binding.rvBanner)
        bannerAdapter.setOnItemClickListener { banner ->
            println("Teste $banner")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}