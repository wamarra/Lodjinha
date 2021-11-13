package br.com.lodjinha.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.lodjinha.R
import br.com.lodjinha.databinding.FragmentProductViewBinding
import br.com.lodjinha.ui.NavigationDelegate
import br.com.lodjinha.utils.DialogUtils
import coil.load

class ProductViewFragment: Fragment() {

    private var _binding: FragmentProductViewBinding? = null
    private val binding: FragmentProductViewBinding get() = _binding!!

    private val args: ProductViewFragmentArgs by lazy {
        ProductViewFragmentArgs.fromBundle(requireArguments())
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
        _binding = FragmentProductViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.setToolbarTitle(args.title)

        _binding?.tvProductCategory?.text = args.tvProductCategory
        _binding?.tvProdcutName?.text = args.tvProdcutName
        _binding?.tvProductPrice2?.text = "Por ${args.tvProductPrice2}"
        _binding?.tvProductPriceFrom2?.text = "De ${args.tvProductPriceFrom2}"
        _binding?.tvProductDescription?.text = args.tvProductDescription

        _binding?.toolbarImage?.apply {
            load(args.urlImagem) {
                crossfade(true)
                placeholder(R.drawable.ic_info)
                error(R.drawable.ic_info)

                listener(onError = {request, throwable ->
                    println(throwable)
                })
            }
        }

        _binding?.onReserve?.setOnClickListener {
            DialogUtils.showDialog(context, getString(R.string.reseverd_product)) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}