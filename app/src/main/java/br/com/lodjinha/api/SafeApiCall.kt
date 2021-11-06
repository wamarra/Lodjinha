package br.com.lodjinha.api

import java.lang.Exception

object SafeApiCall {

    suspend fun <T> safeNetworkRequest(
        networkRequest: suspend () -> T
    ): T? {
        return try {
            networkRequest()
        } catch (ex: Exception) {
            println("Deu ruim")
            println(ex.localizedMessage)
            null
        }
    }
}