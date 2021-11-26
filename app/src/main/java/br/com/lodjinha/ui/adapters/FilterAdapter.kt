package br.com.lodjinha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.lodjinha.databinding.BottomSheetFilterBinding

class FilterAdapter :
    ListAdapter<String, FilterAdapter.FilterViewHolder>(differCallback) {

    inner class FilterViewHolder(
        private val binding: BottomSheetFilterBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.bottomviewTitle.text = title
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(this.layoutPosition)
            }
        }
    }

    companion object {
        private val differCallback: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>(){
                override fun areItemsTheSame(
                    oldItem: String,
                    newItem: String
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: String,
                    newItem: String
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = BottomSheetFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ( (Int) -> Unit )? = null

    fun setOnItemClickListener(clickListener: (Int) -> Unit ) {
        onItemClickListener = clickListener
    }
}