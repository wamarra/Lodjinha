package br.com.lodjinha.models

data class GetBannerResponse(
    val data: List<Banner>
) {
    data class Banner(
        val id: Int = 0,
        val linkUrl: String = "",
        val urlImagem: String = ""
    )
}