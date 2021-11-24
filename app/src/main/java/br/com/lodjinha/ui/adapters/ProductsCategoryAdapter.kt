package br.com.lodjinha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.lodjinha.R
import br.com.lodjinha.databinding.MaisvendidosLayoutRvBinding
import br.com.lodjinha.models.GetProdutosCategoriaResponse
import coil.load

class ProductsCategoryAdapter:
    ListAdapter<GetProdutosCategoriaResponse.ProdutoResponse, ProductsCategoryAdapter.ProductsCategoryViewHolder>(differCallback) {
    inner class ProductsCategoryViewHolder(
        private val binding: MaisvendidosLayoutRvBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GetProdutosCategoriaResponse.ProdutoResponse) {
            binding.apply {
                productIv.load(model.urlImagem) {
                    crossfade(true)
                    placeholder(R.drawable.ic_info)
                    error(R.drawable.ic_info)

                    listener(
                        onSuccess = { request, data ->
                            println("Successo lojinha")
                        },
                        onError = { request, throwable ->
                            println("Error lojinha")
                            println(throwable.localizedMessage)
                            println(request)
                        }
                    )

                }

                descricaoProduto.text = model.descricao

                dePriceProduct.text = "R$ ${model.precoDe}"
                porPriceProduct.text = "R$ ${model.precoPor}"

                root.setOnClickListener {
                    onItemClickListener?.invoke(model)
                }

            }

        }

    }

    companion object {
        private val differCallback: DiffUtil.ItemCallback<GetProdutosCategoriaResponse.ProdutoResponse> =
            object : DiffUtil.ItemCallback<GetProdutosCategoriaResponse.ProdutoResponse>(){
                override fun areItemsTheSame(
                    oldItem: GetProdutosCategoriaResponse.ProdutoResponse,
                    newItem: GetProdutosCategoriaResponse.ProdutoResponse
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: GetProdutosCategoriaResponse.ProdutoResponse,
                    newItem: GetProdutosCategoriaResponse.ProdutoResponse
                ): Boolean {
                    return oldItem.id == newItem.id
                }

            }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsCategoryViewHolder {
        val binding = MaisvendidosLayoutRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductsCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsCategoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ( (GetProdutosCategoriaResponse.ProdutoResponse) -> Unit )? = null

    fun setOnItemClickListener(clickListener: (GetProdutosCategoriaResponse.ProdutoResponse) -> Unit ) {
        onItemClickListener = clickListener
    }

}