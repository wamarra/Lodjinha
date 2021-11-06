package br.com.lodjinha.models

data class GetCategoriaResponse(
    val data: List<Categoria>
) {
    data class Categoria(
        val descricao: String,
        val id: Int,
        val urlImagem: String
    )
}