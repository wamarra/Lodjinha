package br.com.lodjinha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.lodjinha.databinding.BannerLayoutRvBinding
import br.com.lodjinha.models.GetBannerResponse
import coil.load

class BannerAdapter :
    ListAdapter<GetBannerResponse.Banner, BannerAdapter.BannerViewHolder>(differCallback) {

    inner class BannerViewHolder(
        private val binding: BannerLayoutRvBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: GetBannerResponse.Banner) {
            binding.image.apply {
                load(banner.urlImagem) {
                    listener(
                        onSuccess = { request, data ->
                            println(">>>>>>>>>>>>>>>>> Sucesso com o Banner")
                        },
                        onError = { request, throwable ->
                            println(">>>>>>>>>>>>>>>>>>>> " + throwable.message)
                        }
                    )
                }

                setOnClickListener {
                    onItemClickListener?.invoke(banner)
                }

            }

        }

    }

    companion object {
        private val differCallback: DiffUtil.ItemCallback<GetBannerResponse.Banner> =
            object : DiffUtil.ItemCallback<GetBannerResponse.Banner>(){
                override fun areItemsTheSame(
                    oldItem: GetBannerResponse.Banner,
                    newItem: GetBannerResponse.Banner
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: GetBannerResponse.Banner,
                    newItem: GetBannerResponse.Banner
                ): Boolean {
                    return oldItem.id == newItem.id
                }

            }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = BannerLayoutRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ( (GetBannerResponse.Banner) -> Unit )? = null

    fun setOnItemClickListener(clickListener: (GetBannerResponse.Banner) -> Unit ) {
        onItemClickListener = clickListener
    }

}