package com.example.comics.di.factory
import java.io.EOFException
import java.io.IOException
import android.util.MalformedJsonException
import com.example.comics.repository.data.NetworkResponse
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class NetworkResponseCall<T>(
    proxy: Call<T>,
) : CallDelegate<T, NetworkResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResponse<T>>) =
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                val url = response.raw().request.url

                val result = if (response.isSuccessful) {
                    if (body != null) {
                        NetworkResponse.Success(body)
                    } else {
                        responseEmpty()
                    }
                } else {
                    responseError(error, code, url)
                }

                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(result)
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> Failure.NetworkError(throwable = throwable)
                    is HttpException -> convertException(throwable)
                    is EOFException -> Failure.UnparsableResponseException(throwable = throwable)
                    is MalformedJsonException -> Failure.UnparsableResponseException(throwable = throwable)
                    else -> Failure.UnknownError(throwable = throwable)
                }
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResponse.Error(exception = networkResponse))
                )
            }


            private fun convertException(exception: HttpException): Failure {
                return try {
                    val response = getErrorResponse(exception)
                    Failure.UnexpectedApiException(throwable = response.exception)
                } catch (ex: Failure.ClientException) {
                    ex
                } catch (ex: Failure.UnparsableResponseException) {
                    ex
                }
            }

            private fun getErrorResponse(ex: HttpException): NetworkResponse.Error {
                val error = ex.response()?.errorBody()?.string()
                if (error?.isEmpty() != false) {
                    throw Failure.ClientException(throwable = ex)
                }
                return parseError(error, ex)
            }

            private fun parseError(error: String, ex: HttpException): NetworkResponse.Error {
                try {
                    return Gson().fromJson(error, NetworkResponse.Error::class.java)

                } catch (e: Exception) {
                    throw Failure.UnparsableResponseException(throwable = ex)
                }
            }
        })

    fun responseEmpty() = try {
        NetworkResponse.Success(Unit as T)
    } catch (ex: Exception) {
        NetworkResponse.Error(exception = Failure.NoDataContent())
    }

    private fun responseError(
        error: ResponseBody?,
        httpCode: Int,
        url: HttpUrl
    ): NetworkResponse<T> {
        val errorContent = error?.string()

        return if (!errorContent.isNullOrEmpty()) {
            NetworkResponse.Error(
                exception = Failure.RequestError(
                    httpCode.toString(),
                    errorContent,
                    url.toString()
                )
            )
        } else {
            NetworkResponse.Error(
                exception = Failure.UnknownError(
                    httpCode.toString(),
                    url = url.toString()
                )
            )
        }
    }

    override fun cloneImpl(): Call<NetworkResponse<T>> =
        NetworkResponseCall(proxy.clone())
}


abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    override final fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    override final fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
    override fun timeout() = proxy.timeout()
}