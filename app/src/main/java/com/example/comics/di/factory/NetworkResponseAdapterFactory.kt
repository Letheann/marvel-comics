package com.example.comics.di.factory

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import com.example.comics.repository.data.NetworkResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit

class NetworkResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type,
                     annotations: Array<Annotation>,
                     retrofit: Retrofit
    ): CallAdapter<*, *>? {
            return try {

                // check first that the return type is `ParameterizedType`
                check(returnType is ParameterizedType) {
                    "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
                }

                // get the response type inside the `Call` type
                val responseType = getParameterUpperBound(0, returnType)

                // if the response type is not ApiResponse then we can't handle this type, so we return null
                if (getRawType(responseType) != NetworkResponse::class.java) {
                    return null
                }

                // the response type is ApiResponse and should be parameterized
                check(responseType is ParameterizedType) { "Response must be parameterized as NetworkResponse<Foo> or NetworkResponse<out Foo>" }

                val successBodyType = getParameterUpperBound(0, responseType)

                return NetworkResponseAdapter(successBodyType)
            } catch (ex: ClassCastException) {
                null
            }
    }
}