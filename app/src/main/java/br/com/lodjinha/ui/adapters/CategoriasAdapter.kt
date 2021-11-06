package br.com.lodjinha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.lodjinha.R
import br.com.lodjinha.databinding.CategoriesLayoutRvBinding
import br.com.lodjinha.models.GetCategoriaResponse
import coil.load
import coil.transform.CircleCropTransformation

class CategoriasAdapter :
    ListAdapter<GetCategoriaResponse.Categoria, CategoriasAdapter.CategoriaViewHolder>(differCallback) {

    inner class CategoriaViewHolder(
        private val binding: CategoriesLayoutRvBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(categoria: GetCategoriaResponse.Categoria) {
            binding.categoryIv.apply {
                load(categoria.urlImagem) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_info)
                    error(R.drawable.ic_info)

                    listener(onError = {request, throwable ->
                        println(throwable)
                    })
                }

                setOnClickListener {
                    onItemClickListener?.invoke(categoria)
                }
            }

            binding.categorieTitleText.text = categoria.descricao
        }
    }

    companion object {
        private val differCallback: DiffUtil.ItemCallback<GetCategoriaResponse.Categoria> =
            object : DiffUtil.ItemCallback<GetCategoriaResponse.Categoria>(){
                override fun areItemsTheSame(
                    oldItem: GetCategoriaResponse.Categoria,
                    newItem: GetCategoriaResponse.Categoria
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: GetCategoriaResponse.Categoria,
                    newItem: GetCategoriaResponse.Categoria
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = CategoriesLayoutRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ( (GetCategoriaResponse.Categoria) -> Unit )? = null

    fun setOnItemClickListener(clickListener: (GetCategoriaResponse.Categoria) -> Unit ) {
        onItemClickListener = clickListener
    }
}