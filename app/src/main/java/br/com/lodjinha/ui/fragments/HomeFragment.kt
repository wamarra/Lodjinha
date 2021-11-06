package br.com.lodjinha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import br.com.lodjinha.api.RetrofitInstance
import br.com.lodjinha.databinding.FragmentHomeBinding
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.ui.adapters.BannerAdapter
import br.com.lodjinha.ui.adapters.CategoriasAdapter
import br.com.lodjinha.ui.adapters.ProductsAdapter
import br.com.lodjinha.ui.viewmodels.MainViewModel
import br.com.lodjinha.ui.viewmodels.MainViewModelProviderFactory

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

        viewModel.getBanner()

        viewModel.getCategories()

        viewModel.getMaisVendidos()

    }

    private fun setupMaisVendidosRv() {
        maisVendidosAdapter = ProductsAdapter()
        binding.maisVendidosRv.adapter = maisVendidosAdapter
        maisVendidosAdapter.setOnItemClickListener { produtoResponse ->
            println(produtoResponse)
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
        viewModel.bannerLiveData.observe(viewLifecycleOwner) { list ->
            bannerAdapter.differ.submitList(list)
        }

        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { list ->
            categoriasAdapter.differ.submitList(list)
        }

        viewModel.maisVendidosLiveData.observe(viewLifecycleOwner) { list ->
            maisVendidosAdapter.differ.submitList(list)
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
