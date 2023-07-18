package com.example.comics.di.factory

sealed class Failure(
    open val codeStatusBackEnd: String? = "",
    val errorMessage: String = "",
    open val codeStatusResponse: String? = "",
    open val url: String? = ""
) : java.lang.Exception() {

    data class NoDataContent(
        override val codeStatusBackEnd: String? = null,
        val errorMessageCustom: String? = null
    ) : Failure(codeStatusBackEnd, errorMessageCustom ?: "No data content")

    data class RequestError(
        override val codeStatusResponse: String? = null,
        private val msg: String? = null,
        override val url: String? = ""
    ) : Failure(
        errorMessage = msg ?: "Error loading data!",
        codeStatusResponse = codeStatusResponse,
        url = url
    )

    data class NetworkError(
        override val codeStatusBackEnd: String? = "-1200",
        private val throwable: Throwable
    ) : Failure(
        codeStatusBackEnd,
        "Sem conexão. Verifique o wifi ou dados móveis e tente novamente em alguns instantes."
    )

    data class UnknownError(
        override val codeStatusBackEnd: String? = null,
        private val throwable: Throwable? = Exception(),
        override val url: String? = ""
    ) : Failure(codeStatusBackEnd, throwable?.message ?: MSG_DEFAULT, codeStatusBackEnd)

    data class UnexpectedApiException(
        override val codeStatusBackEnd: String? = "-1011",
        private val throwable: Throwable? = Exception()
    ) : Failure(
        codeStatusBackEnd, throwable?.message
            ?: MSG_DEFAULT
    )

    data class ClientException(
        override val codeStatusBackEnd: String? = "-1011",
        private val throwable: Throwable? = Exception()
    ) : Failure(
        codeStatusBackEnd, throwable?.message
            ?: MSG_DEFAULT
    )

    data class UnparsableResponseException(
        override val codeStatusBackEnd: String? = "-1012",
        private val throwable: Throwable? = Exception()
    ) : Failure(
        codeStatusBackEnd, throwable?.message
            ?: MSG_DEFAULT
    )

    abstract class FeatureFailure(
        errorMessage: String? = "",
        codeStatusResponse: String? = "",
        codeStatusBackEnd: String? = ""
    ) : Failure(
        codeStatusResponse = codeStatusResponse,
        codeStatusBackEnd = codeStatusBackEnd,
        errorMessage = errorMessage ?: ""
    )

    companion object {
        private const val MSG_DEFAULT =
            "Erro Tente novamente em alguns instantes."
    }
}