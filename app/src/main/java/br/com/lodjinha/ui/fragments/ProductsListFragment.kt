package br.com.lodjinha.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.lodjinha.api.RetrofitInstance
import br.com.lodjinha.databinding.FragmentProductsListBinding
import br.com.lodjinha.models.GetProdutosCategoriaResponse
import br.com.lodjinha.repositories.LodjinhaRepository
import br.com.lodjinha.ui.NavigationDelegate
import br.com.lodjinha.ui.adapters.ProductsCategoryAdapter
import br.com.lodjinha.ui.components.FilterBottomSheetDialog.Companion.openBottomSheetDialog
import br.com.lodjinha.ui.viewmodels.ProductCategoryViewModel
import br.com.lodjinha.ui.viewmodels.ProductCategoryViewModelProviderFactory
import br.com.lodjinha.utils.toggleVisibilty

class ProductsListFragment : Fragment() {

    private var _binding: FragmentProductsListBinding? = null
    private val binding: FragmentProductsListBinding get() = _binding!!

    private val args: ProductsListFragmentArgs by lazy {
        ProductsListFragmentArgs.fromBundle(requireArguments())
    }

    private lateinit var productsCategoryAdapter: ProductsCategoryAdapter

    private val viewModel: ProductCategoryViewModel by lazy {
        val repository = LodjinhaRepository(RetrofitInstance.apiService)
        val viewModelProviderFactory = ProductCategoryViewModelProviderFactory(repository)
        ViewModelProvider(this, viewModelProviderFactory).get(ProductCategoryViewModel::class.java)
    }

    private var listener: NavigationDelegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationDelegate) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setToolbarTitle(args.title)
        setupProductList()
        setupObservers()
        viewModel.getProductsByCategoryData(args.categoryId)
    }

    private fun setupProductList() {
        productsCategoryAdapter = ProductsCategoryAdapter()
        binding.produtosRv.adapter = productsCategoryAdapter
        productsCategoryAdapter.setOnItemClickListener { productsCategoryResponse ->

            findNavController().navigate(
                ProductsListFragmentDirections.actionProductsListFragmentToProductViewFragment(
                    title = productsCategoryResponse.nome,
                    productId = productsCategoryResponse.id,
                    tvProductCategory = productsCategoryResponse.categoria.descricao,
                    tvProdcutName = productsCategoryResponse.nome,
                    tvProductPrice2 = productsCategoryResponse.precoPor.toString(),
                    tvProductPriceFrom2 = productsCategoryResponse.precoDe.toString(),
                    tvProductDescription = productsCategoryResponse.descricao,
                    urlImagem = productsCategoryResponse.urlImagem
                )
            )
        }
    }

    private fun setupObservers() {
        viewModel.productCategoryDataLiveData.observe(viewLifecycleOwner) { viewState ->
            when {
                viewState.loading -> {
                    binding.produtosRv.toggleVisibilty(false)
                    binding.progress.toggleVisibilty(true)
                }
                viewState.error -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionMainFragmentToErrorFragment()
                    )
                }
                viewState.data != null -> {
                    binding.progress.toggleVisibilty(false)
                    binding.produtosRv.toggleVisibilty(true)
                    if (viewState.data?.productsCategoryData.isNullOrEmpty().not()) {
                        var products = viewState.data?.productsCategoryData
                        productsCategoryAdapter.differ.submitList(products)

                        createSheetMenu(products)
                    }
                }
            }
        }
    }

    private fun createSheetMenu(products: List<GetProdutosCategoriaResponse.ProdutoResponse>?) {
        listener?.menuFilter?.apply {
            setOnMenuItemClickListener {
                openBottomSheetDialog(getFilterItems()) { _, id ->
                    when (id) {
                        0 -> {
                            productsCategoryAdapter.differ.submitList(products?.sortedBy { it.nome })
                        }
                        1 -> {
                            productsCategoryAdapter.differ.submitList(products?.sortedByDescending { it.nome })
                        }
                        2 -> {
                            productsCategoryAdapter.differ.submitList(products)
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun getFilterItems(): ArrayList<String> {
        return arrayListOf("A - Z", "Z - A", "Original")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}